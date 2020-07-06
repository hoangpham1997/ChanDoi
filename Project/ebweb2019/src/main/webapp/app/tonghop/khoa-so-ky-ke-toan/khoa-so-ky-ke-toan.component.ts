import { Component, OnInit, ViewChild } from '@angular/core';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Principal } from 'app/core';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { JhiEventManager } from 'ng-jhipster';
import { RefModalService } from 'app/core/login/ref-modal.service';
import { XuLyChungTuComponent } from 'app/tonghop/khoa-so-ky-ke-toan/xu-ly-chung-tu.component';
import * as moment from 'moment';
import { Moment } from 'moment';
import { DBDateClosed } from 'app/app.constants';
import { DATE_FORMAT, ITEMS_PER_PAGE } from 'app/shared';
import { XuLyChungTuService } from 'app/tonghop/khoa-so-ky-ke-toan/xu-ly-chung-tu.service';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ViewVoucherNo } from 'app/tonghop/khoa-so-ky-ke-toan/view-voucher-no.model';
import { UpdateDateClosedBook } from 'app/tonghop/bo-khoa-so-ky-ke-toan/update-date-closed-book.model';
import { BoKhoaSoKyKeToanService } from 'app/tonghop/bo-khoa-so-ky-ke-toan/bo-khoa-so-ky-ke-toan.service';
import { OrganizationUnitService } from 'app/danhmuc/organization-unit';
import { TreeviewItem } from 'app/shared/tree-combo-box/tree-item';
import { OrgTreeDtoModel } from 'app/tonghop/khoa-so-ky-ke-toan/org-tree-dto.model';
import { IAccountList } from 'app/shared/model/account-list.model';

@Component({
    selector: 'eb-khoa-so-ky-ke-toan',
    templateUrl: './khoa-so-ky-ke-toan.component.html',
    styleUrls: ['./khoa-so-ky-ke-toan.component.css']
})
export class KhoaSoKyKeToanComponent implements OnInit {
    @ViewChild('content') public modalContent: NgbModalRef;
    modalRef: NgbModalRef;
    modalRefMess: NgbModalRef;
    dateLockOld: Moment;
    dateLockNew: Moment;
    account: any;
    itemsPerPage: any;
    data: any;
    count: number;
    organizationUnit: TreeviewItem;
    treeOrganizationUnits: any[];
    orgTreeDtoModel: OrgTreeDtoModel[];
    lstBranch: any;

    constructor(
        public activeModal: NgbActiveModal,
        private principal: Principal,
        private toastr: ToastrService,
        public translate: TranslateService,
        public eventManager: JhiEventManager,
        private refModalService: RefModalService,
        private xuLyChungTuService: XuLyChungTuService,
        private boKhoaSoKyKeToanService: BoKhoaSoKyKeToanService,
        private modalService: NgbModal,
        private organizationUnitService: OrganizationUnitService
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.principal.identity().then(account => {
            this.account = account;
            if (account) {
                const dbDateClosed = this.account.systemOption.find(x => x.code === DBDateClosed).data;
                this.dateLockOld = dbDateClosed ? moment(dbDateClosed, DATE_FORMAT) : null;
                if (dbDateClosed) {
                    this.dateLockNew = dbDateClosed
                        ? moment(
                              `${moment(dbDateClosed, DATE_FORMAT)
                                  .add(1, 'years')
                                  .year()}-01-01`,
                              DATE_FORMAT
                          )
                        : null;
                } else {
                    const now = moment(new Date(), DATE_FORMAT);
                    this.dateLockNew = moment(`${now.year() + 1}-01-01`, DATE_FORMAT);
                }
            }
        });
        this.organizationUnitService.getChildCompanyByID().subscribe((res: HttpResponse<any>) => {
            this.treeOrganizationUnits = res.body.orgTrees;
            this.organizationUnit = res.body.currentOrgLogin;
            if (this.treeOrganizationUnits && this.treeOrganizationUnits.length > 0 && this.treeOrganizationUnits[0].children.length > 0) {
                this.orgTreeDtoModel = this.treeOrganizationUnits[0].children;
            }
        });
    }

    ngOnInit() {}

    accept() {
        if (this.checkErr()) {
            this.lstBranch = this.orgTreeDtoModel
                ? this.orgTreeDtoModel.filter(n => n.checked).length > 0
                    ? JSON.stringify(this.orgTreeDtoModel.filter(n => n.checked))
                    : null
                : null;
            this.xuLyChungTuService
                .getAllVoucherNotRecorded({
                    page: 0,
                    size: this.itemsPerPage,
                    postedDate: this.dateLockNew.format(DATE_FORMAT),
                    listBranch: this.lstBranch
                })
                .subscribe(
                    (res: HttpResponse<ViewVoucherNo[]>) => {
                        if (res.body.length > 0) {
                            this.count = parseInt(res.headers.get('X-Total-Count'), 10);
                            this.data = res;
                            this.modalRefMess = this.modalService.open(this.modalContent, { backdrop: 'static' });
                        } else {
                            const updateDateCloseBook: UpdateDateClosedBook = {};
                            updateDateCloseBook.dateClosedBookOld = this.dateLockOld;
                            updateDateCloseBook.dateClosedBook = this.dateLockNew;
                            this.boKhoaSoKyKeToanService.updateDateClosedBook(updateDateCloseBook).subscribe(
                                (resUD: HttpResponse<boolean>) => {
                                    if (resUD.body) {
                                        this.onSuccess();
                                    } else {
                                        this.error();
                                    }
                                },
                                (resUD: HttpErrorResponse) => this.onError(resUD.message)
                            );
                        }
                    },
                    (res: HttpErrorResponse) => this.onError(res.error.title)
                );
        }
    }

    private error() {
        this.toastr.error(this.translate.instant('ebwebApp.lockBook.lockErr'), this.translate.instant('ebwebApp.mCReceipt.home.message'));
        this.activeModal.close();
    }

    private onSuccess() {
        // Load lại systemOption
        this.principal.identity(true).then(account => {
            this.eventManager.broadcast({
                name: 'lockSuccess'
            });
        });
        this.toastr.success(
            this.translate.instant('ebwebApp.lockBook.lockSuccess'),
            this.translate.instant('ebwebApp.mCReceipt.home.message')
        );
        this.activeModal.close();
    }

    checkErr() {
        if (!this.dateLockNew) {
            this.toastr.error(this.translate.instant('global.data.null'), this.translate.instant('ebwebApp.mCReceipt.error.error'));
            return false;
        } else if (this.dateLockOld && this.dateLockOld.format(DATE_FORMAT) === this.dateLockNew.format(DATE_FORMAT)) {
            this.toastr.error(
                this.translate.instant('ebwebApp.lockBook.err.postedDate'),
                this.translate.instant('ebwebApp.mCReceipt.error.error')
            );
            return false;
        } else if (this.dateLockOld && this.dateLockOld.format(DATE_FORMAT) > this.dateLockNew.format(DATE_FORMAT)) {
            this.toastr.error(
                this.translate.instant('ebwebApp.lockBook.err.postedDate'),
                this.translate.instant('ebwebApp.mCReceipt.error.error')
            );
            return false;
        }
        return true;
    }

    private onError(errorMessage) {
        this.toastr.error(errorMessage, this.translate.instant('ebwebApp.mCReceipt.error.error'));
    }

    openPopupXuLyGhiSo() {
        this.modalRefMess.close();
        this.activeModal.dismiss('closed');
        this.modalRef = this.refModalService.open(
            {
                postedDate: this.dateLockNew,
                lstBranch: this.lstBranch
            },
            XuLyChungTuComponent,
            this.data,
            false
        );
    }

    isCheckAll() {
        if (this.orgTreeDtoModel) {
            return this.orgTreeDtoModel.every(item => item.checked) && this.orgTreeDtoModel.length;
        } else {
            return false;
        }
    }

    checkAll() {
        const isCheck = this.orgTreeDtoModel.every(item => item.checked) && this.orgTreeDtoModel.length;
        this.orgTreeDtoModel.forEach(item => (item.checked = !isCheck));
    }

    check(orgTreeDtoModel1: OrgTreeDtoModel) {
        orgTreeDtoModel1.checked = !orgTreeDtoModel1.checked;
    }

    newArr(lenght: number): any[] {
        if (lenght > 0) {
            return new Array(lenght);
        } else {
            return new Array(0);
        }
    }
}
