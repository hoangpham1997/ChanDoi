import { Injectable } from '@angular/core';
import { SERVER_API_URL } from 'app/app.constants';
import { HttpClient, HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { createRequestOption, DATE_FORMAT } from 'app/shared';
import { catchError } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { RequestReport } from 'app/bao-cao/reqest-report.model';
import { ViewVoucherNo } from 'app/tonghop/khoa-so-ky-ke-toan/view-voucher-no.model';

@Injectable({ providedIn: 'root' })
export class BaoCaoService {
    private resourceUrl = SERVER_API_URL + 'api/business/report';
    private instructionUrl = SERVER_API_URL + 'api/business/instruction';
    private sessionData: { type: string; data: any }[] = [];
    constructor(private http: HttpClient) {}

    public getSessionData(type: string) {
        this.sessionData = JSON.parse(sessionStorage.getItem('searchReportSession')) || [];
        this.updateHiddenDependent();
        const index = this.sessionData.findIndex(x => x.type === type);
        return index >= 0 ? this.sessionData[index] : { type, data: {} };
    }

    public changeCompany() {
        this.sessionData = JSON.parse(sessionStorage.getItem('searchReportSession')) || [];
        this.sessionData.forEach(item => {
            item.data.organizationUnit = null;
            item.data.dependent = false;
        });
        sessionStorage.setItem('searchReportSession', JSON.stringify(this.sessionData));
    }

    public putSessionData(value: { type: string; data: any }) {
        const index = this.sessionData.findIndex(x => x.type === value.type);
        if (index >= 0) {
            this.sessionData[index].data = value.data;
        } else {
            this.sessionData.push(value);
        }
        this.updateHiddenDependent();
        sessionStorage.setItem('searchReportSession', JSON.stringify(this.sessionData));
    }

    updateHiddenDependent() {
        for (let i = 0; i < this.sessionData.length; i++) {
            if (this.sessionData[i].data.organizationUnit) {
                if (!this.sessionData[i].data.organizationUnit.parentCompany) {
                    this.sessionData[i].data.hiddenDependent = true;
                } else {
                    let isHidden = true;
                    for (let j = 0; j < this.sessionData[i].data.organizationUnit.children.length; j++) {
                        if (this.sessionData[i].data.organizationUnit.children[j].accType === 0) {
                            isHidden = false;
                        }
                    }
                    this.sessionData[i].data.hiddenDependent = isHidden;
                }
            }
        }
    }

    checkHiddenDependent(datas) {
        for (let i = 0; i < datas.length; i++) {
            if (datas[i]) {
                if (datas[i].parent.unitType !== 0 || (datas[i].parent.unitType === 0 && !datas[i].children)) {
                    datas[i].hiddenDependent = true;
                } else {
                    for (let j = 0; j < datas[i].children.length; j++) {
                        if (
                            datas[i].children[j].children &&
                            datas[i].children[j].children.length > 0 &&
                            datas[i].children[j].parent.unitType === 0 &&
                            datas[i].children[j].children.some(x => x.parent.accType === 0)
                        ) {
                            datas[i].children[j].hiddenDependent = false;
                            for (let k = 0; k < datas[i].children[j].children.length; k++) {
                                datas[i].children[j].children[k].hiddenDependent = true;
                            }
                        } else {
                            datas[i].children[j].hiddenDependent = true;
                            if (datas[i].children[j].children && datas[i].children[j].children.length > 0) {
                                for (let k = 0; k < datas[i].children[j].children.length; k++) {
                                    datas[i].children[j].children[k].hiddenDependent = true;
                                }
                            }
                        }
                    }
                    if (datas[i].children.some(x => x.parent.accType === 0)) {
                        datas[i].hiddenDependent = false;
                    } else {
                        datas[i].hiddenDependent = true;
                    }
                }
            }
        }
    }

    checkHiddenFirstCompany(data) {
        if (
            data.parent.unitType !== 0 ||
            (data.parent.unitType === 0 && data.children && !data.children.some(x => x.parent.accType === 0)) ||
            (data.parent.unitType === 0 && !data.children)
        ) {
            data.hiddenDependent = true;
        } else {
            data.hiddenDependent = false;
        }
    }

    /*
    * Author Hautv
    * Get flie báo cáo
    * */
    public reportPDF(requestReport: RequestReport) {
        const copy = this.convertDateFromClient(requestReport);
        let headers = new HttpHeaders();
        headers = headers.set('Accept', 'application/pdf');
        return this.http
            .post(this.resourceUrl, copy, {
                observe: 'response',
                headers,
                responseType: 'blob'
            })
            .pipe(catchError(this.parseErrorBlob));
    }

    private parseErrorBlob(err: HttpErrorResponse): Observable<any> {
        const reader: FileReader = new FileReader();

        const obs = Observable.create((observer: any) => {
            reader.onloadend = () => {
                observer.error(JSON.parse(reader.result));
                observer.complete();
            };
        });
        reader.readAsText(err.error);
        return obs;
    }

    private convertDateFromClient(requestReport: RequestReport): ViewVoucherNo {
        const copy: ViewVoucherNo = Object.assign({}, requestReport, {
            fromDate: requestReport.fromDate != null ? requestReport.fromDate.format(DATE_FORMAT) : null,
            toDate: requestReport.toDate != null ? requestReport.toDate.format(DATE_FORMAT) : null
        });
        return copy;
    }

    getReport(requestReport) {
        this.reportPDF(requestReport).subscribe(response => {
            // this.showReport(response);
            const file = new Blob([response.body], { type: 'application/pdf' });
            const fileURL = window.URL.createObjectURL(file);
            const isDownload = false; // mặc định open new tab
            if (isDownload) {
                const link = document.createElement('a');
                document.body.appendChild(link);
                link.download = fileURL;
                link.setAttribute('style', 'display: none');
                const name = 'ten_bao_cao.pdf';
                link.setAttribute('download', name);
                link.href = fileURL;
                link.click();
            } else {
                const contentDispositionHeader = response.headers.get('Content-Disposition');
                const result = contentDispositionHeader
                    .split(';')[1]
                    .trim()
                    .split('=')[1];
                const newWin = window.open(fileURL, '_blank');
                // add a load listener to the window so that the title gets changed on page load
                newWin.addEventListener('load', () => {
                    newWin.document.title = result.replace(/'/g, '');
                });
            }
        });
    }

    getReportHDDT(requestReport: RequestReport): Observable<any> {
        return this.http.post(SERVER_API_URL + 'api/e-invoices/report', requestReport, {
            observe: 'response',
            responseType: 'text'
        });
    }

    getInstruction() {
        this.viewInvoicePdf().subscribe(response => {
            // this.showReport(response);
            const file = new Blob([response.body], { type: 'application/pdf' });
            const fileURL = window.URL.createObjectURL(file);
            window.open(fileURL, '_blank');
        });
    }

    viewInvoicePdf(): Observable<HttpResponse<any>> {
        let headers = new HttpHeaders();
        headers = headers.set('Accept', 'application/pdf');
        return this.http
            .get(this.instructionUrl, {
                observe: 'response',
                headers,
                responseType: 'blob'
            })
            .pipe(catchError(this.parseErrorBlob));
    }

    exportExcel(requestReport): Observable<any> {
        const copy = this.convertDateFromClient(requestReport);
        return this.http
            .post(this.resourceUrl + '/excel', copy, {
                observe: 'response',
                responseType: 'blob'
            })
            .pipe(catchError(this.parseErrorBlob));
    }
}
