import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ViewVoucherNo } from 'app/tonghop/khoa-so-ky-ke-toan/view-voucher-no.model';
import { DataBackupModel } from './data-backup.model';

type EntityResponseType = HttpResponse<DataBackupModel>;
type EntityArrayResponseType = HttpResponse<DataBackupModel[]>;

@Injectable({ providedIn: 'root' })
export class DataBackupService {
    private resourceUrl = SERVER_API_URL + 'api/data';

    constructor(private http: HttpClient) {}

    find(id: string): Observable<EntityResponseType> {
        return this.http
            .get<DataBackupModel>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    backupData(): Observable<any> {
        return this.http.post<any>(this.resourceUrl + '/backup', {}, { observe: 'response' });
    }

    delete(rq): Observable<any> {
        return this.http.post<any>(this.resourceUrl + '/delete', rq, { observe: 'response' });
    }

    restore(rq): Observable<any> {
        const headers = new HttpHeaders({
            'Content-Type': 'application/json'
        });
        return this.http.post<any>(this.resourceUrl + '/restore', rq, { observe: 'response' });
    }

    getAllDataBackup(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<object[]>(this.resourceUrl + '/get-all-data-backup', { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    getAllDataRestore(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<object[]>(this.resourceUrl + '/get-all-data-restore', { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    private convertDateFromClient(xuLyChungTuModel: ViewVoucherNo): ViewVoucherNo {
        const copy: ViewVoucherNo = Object.assign({}, xuLyChungTuModel, {
            date: xuLyChungTuModel.date != null && xuLyChungTuModel.date.isValid() ? xuLyChungTuModel.date.format(DATE_FORMAT) : null,
            postedDate:
                xuLyChungTuModel.postedDate != null && xuLyChungTuModel.postedDate.isValid()
                    ? xuLyChungTuModel.postedDate.format(DATE_FORMAT)
                    : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.dateBackup = res.body.dateBackup != null ? moment(res.body.dateBackup) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((dataBackupModel: DataBackupModel) => {
            dataBackupModel.dateBackup = dataBackupModel.dateBackup != null ? moment(dataBackupModel.dateBackup) : null;
            dataBackupModel.timeRestore = dataBackupModel.timeRestore != null ? moment(dataBackupModel.timeRestore) : null;
        });
        return res;
    }
}
