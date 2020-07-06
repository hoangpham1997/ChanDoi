import { Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DataBackupModel } from 'app/data/data-backup.model';
import { DataBackupService } from 'app/data/data-backup.service';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ROLE } from 'app/role.constants';
import { IMCReceipt } from 'app/shared/model/mc-receipt.model';
import { ITEMS_PER_PAGE } from 'app/shared';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';
import { Principal } from 'app/core';

@Component({
    selector: 'eb-data-backup',
    templateUrl: './data-backup.component.html',
    styleUrls: ['./data-backup.component.css']
})
export class DataBackupComponent extends BaseComponent implements OnInit {
    @ViewChild('content') public modalContent: NgbModalRef;
    datas: DataBackupModel[];
    datasRestore: DataBackupModel[];
    file: any;
    refModel: any;
    link: any;
    selectedRow: DataBackupModel;
    modalRef: NgbModalRef;
    question: string;
    ROLE = ROLE;
    selectT?: any = 0;
    totalItemsBackup: any;
    queryCountBackup: any;
    itemsPerPageBackup: any;
    pageBackup: any;
    predicateBackup: any;
    previousPageBackup: any;

    totalItemsRestore: any;
    queryCountRestore: any;
    itemsPerPageRestore: any;
    pageRestore: any;
    predicateRestore: any;
    previousPageRestore: any;
    account: any;

    constructor(
        private router: Router,
        private modalService: NgbModal,
        private backupService: DataBackupService,
        private toastr: ToastrService,
        public translate: TranslateService,
        private principal: Principal
    ) {
        super();
        this.datas = [];
        this.principal.identity().then(account => {
            this.account = account;
        });
    }

    ngOnInit() {
        this.pageBackup = 1;
        this.itemsPerPageBackup = ITEMS_PER_PAGE;
        this.pageRestore = 1;
        this.itemsPerPageRestore = ITEMS_PER_PAGE;
        this.loadBackup();
        this.loadRestore();
    }

    loadBackup() {
        this.backupService
            .getAllDataBackup({
                page: this.pageBackup - 1,
                size: this.itemsPerPageBackup
            })
            .subscribe((res: HttpResponse<DataBackupModel[]>) => {
                this.datas = res.body;
                this.paginateBackup(res.body, res.headers);
            });
    }

    loadRestore() {
        this.backupService
            .getAllDataRestore({
                page: this.pageRestore - 1,
                size: this.itemsPerPageRestore
            })
            .subscribe((res: HttpResponse<DataBackupModel[]>) => {
                this.datasRestore = res.body;
                this.paginateRestore(res.body, res.headers);
            });
    }

    trackId(index: number, item: DataBackupModel) {
        return item.name;
    }

    newArr(lenght: number): any[] {
        if (lenght > 0) {
            return new Array(lenght);
        } else {
            return new Array(0);
        }
    }

    closeForm() {
        event.preventDefault();
        if (!this.modalRef) {
            this.router.navigate(['']);
        }
    }

    importData(detailModalLoan) {
        this.refModel = this.modalService.open(detailModalLoan, { size: 'lg' });
    }

    onSelect(data) {
        this.selectedRow = data;
    }

    upload(download) {
        /*this.isLoading = true;
        this.saBillService.upload(this.file).subscribe(
            res => {
                if (res.headers.get('isError') === '1') {
                    if (res.headers.get('message')) {
                        this.toastrService.error(this.translateService.instant('ebwebApp.saBill.upload.' + res.headers.get('message')));
                    } else {
                        // this.refModel.close();
                        this.modalService.open(download, { size: 'lg' });
                        const blob = new Blob([res.body], { type: 'application/pdf' });
                        const fileURL = URL.createObjectURL(blob);

                        this.link = document.createElement('a');
                        document.body.appendChild(this.link);
                        this.link.download = fileURL;
                        this.link.setAttribute('style', 'display: none');
                        const name = 'excel.xls';
                        this.link.setAttribute('download', name);
                        this.link.href = fileURL;
                    }
                } else {
                    this.loadPage(0);
                    this.refModel.close();
                    this.toastrService.success(this.translateService.instant('ebwebApp.saBill.upload.success'));
                }
                this.isLoading = false;
            },
            () => {
                this.isLoading = false;
            }
        );*/
    }

    changeFile(event) {
        const file = event.target.files;
        this.file = null;
        if (file && file.length) {
            this.file = file[0];
        }
    }

    download() {
        this.link.click();
    }

    backupData() {
        this.question = 'backup';
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.modalRef = this.modalService.open(this.modalContent, { backdrop: 'static' });
    }

    backupSuccess() {
        this.toastr.success(
            this.translate.instant('ebwebApp.dataBackup.mess.backupSuccsess'),
            this.translate.instant('ebwebApp.mBDeposit.message')
        );
    }

    deleteSuccess() {
        this.toastr.success(
            this.translate.instant('ebwebApp.dataBackup.mess.deleteSuccsess'),
            this.translate.instant('ebwebApp.mBDeposit.message')
        );
    }

    deleteFail() {
        this.toastr.error(
            this.translate.instant('ebwebApp.dataBackup.mess.deleteFail'),
            this.translate.instant('ebwebApp.mBDeposit.message')
        );
    }

    restoreSuccess() {
        this.toastr.success(
            this.translate.instant('ebwebApp.dataBackup.mess.restoreSuccsess'),
            this.translate.instant('ebwebApp.mBDeposit.message')
        );
    }

    restoreFail() {
        this.toastr.error(
            this.translate.instant('ebwebApp.dataBackup.mess.restoreFail'),
            this.translate.instant('ebwebApp.mBDeposit.message')
        );
    }

    backupFail() {
        this.toastr.error(
            this.translate.instant('ebwebApp.dataBackup.mess.backupFail'),
            this.translate.instant('ebwebApp.mBDeposit.message')
        );
    }

    round(number) {
        if (number) {
            return Math.round(number);
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.QLDuLieu_Xoa])
    delete() {
        event.preventDefault();
        this.question = 'delete';
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.modalRef = this.modalService.open(this.modalContent, { backdrop: 'static' });
    }

    restoreData() {
        this.question = 'restore';
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.modalRef = this.modalService.open(this.modalContent, { backdrop: 'static' });
    }

    acceptBackup() {
        this.backupService.backupData().subscribe(
            (res: HttpResponse<any>) => {
                if (res.body) {
                    this.datas = res.body;
                    if (this.datas && this.datas.length > 0) {
                        this.loadBackup();
                        this.backupSuccess();
                    }
                }
            },
            (res: HttpErrorResponse) => {
                this.backupFail();
            }
        );
    }

    acceptDelete() {
        if (this.selectedRows.length > 0) {
            this.backupService.delete(this.selectedRows.map(n => n.id)).subscribe(
                (res: HttpResponse<any>) => {
                    if (res.body) {
                        this.loadRestore();
                        this.loadBackup();
                        this.deleteSuccess();
                    }
                },
                (res: HttpErrorResponse) => {
                    this.deleteFail();
                }
            );
        }
    }

    acceptRestoreData() {
        if (this.selectedRows.length === 1) {
            this.backupService.restore(this.selectedRows.map(n => n.id)).subscribe(
                (res: HttpResponse<any>) => {
                    if (res.body) {
                        this.restoreSuccess();
                        this.loadRestore();
                        this.loadBackup();
                    }
                },
                (res: HttpErrorResponse) => {
                    this.restoreFail();
                }
            );
        }
    }

    accept() {
        switch (this.question) {
            case 'backup':
                this.acceptBackup();
                break;
            case 'restore':
                this.acceptRestoreData();
                break;
            case 'delete':
                this.acceptDelete();
                break;
        }
        this.modalRef.close();
        this.modalRef = undefined;
    }

    modalRefClose() {
        this.modalRef.close();
        this.modalRef = undefined;
    }

    getStatus(status: number): string {
        switch (status) {
            case 0:
                return 'Thành công';
            case 1:
                return 'Không thành công';
            default:
                return 'Thành công';
        }
    }

    selectTab($event) {
        if ($event.nextId === 'tab0') {
            this.selectT = 0;
        } else {
            this.selectT = 1;
        }
    }

    private paginateBackup(data: DataBackupModel[], headers: HttpHeaders) {
        // this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItemsBackup = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCountBackup = this.totalItemsBackup;
        this.datas = data;
        this.objects = data;
        this.selectedRows = [];
        if (this.datas && this.datas.length > 0) {
            this.selectedRow = this.datas[0];
            this.objects = this.datas;
            this.selectedRows.push(this.selectedRow);
        }
    }

    private paginateRestore(data: DataBackupModel[], headers: HttpHeaders) {
        this.totalItemsRestore = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCountRestore = this.totalItemsRestore;
        this.datasRestore = data;
    }

    loadPageBackup(page: number) {
        this.selectedRows = [];
        this.pageBackup = page;
        this.loadBackup();
    }

    selectedItemPerPageBackup() {
        this.loadBackup();
    }

    loadPageRestore(page: number) {
        this.pageRestore = page;
        this.loadRestore();
    }

    selectedItemPerPageRestore() {
        this.loadRestore();
    }
}
