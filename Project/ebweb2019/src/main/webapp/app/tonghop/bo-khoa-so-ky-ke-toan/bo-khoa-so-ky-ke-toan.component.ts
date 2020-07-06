import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Principal } from 'app/core';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { JhiEventManager } from 'ng-jhipster';
import { DBDateClosed, DBDateClosedOld, NGAY_HACH_TOAN } from 'app/app.constants';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared';
import { Moment } from 'moment';
import { UpdateDateClosedBook } from 'app/tonghop/bo-khoa-so-ky-ke-toan/update-date-closed-book.model';
import { BoKhoaSoKyKeToanService } from 'app/tonghop/bo-khoa-so-ky-ke-toan/bo-khoa-so-ky-ke-toan.service';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';

@Component({
    selector: 'eb-bo-khoa-so-ky-ke-toan',
    templateUrl: 'bo-khoa-so-ky-ke-toan.component.html',
    styleUrls: ['bo-khoa-so-ky-ke-toan.component.css']
})
export class BoKhoaSoKyKeToanComponent implements OnInit {
    dateLockNow: Moment;
    dateLockNew: Moment;
    account: any;
    updateDateCloseBook: UpdateDateClosedBook;

    constructor(
        public activeModal: NgbActiveModal,
        private principal: Principal,
        private toastr: ToastrService,
        public translate: TranslateService,
        public eventManager: JhiEventManager,
        public boKhoaSoKyKeToanService: BoKhoaSoKyKeToanService
    ) {}

    ngOnInit(): void {
        this.updateDateCloseBook = {};
        this.principal.identity().then(account => {
            this.account = account;
            if (account) {
                const dbDateClosed = this.account.systemOption.find(x => x.code === DBDateClosed).data;
                const dbDateClosedOld = this.account.systemOption.find(x => x.code === DBDateClosedOld).data;
                this.dateLockNow = dbDateClosed ? moment(dbDateClosed, DATE_FORMAT) : null;
                this.dateLockNew = dbDateClosedOld ? moment(dbDateClosedOld, DATE_FORMAT) : null;
                if (!this.dateLockNew) {
                    const postedDate = this.account.systemOption.find(x => x.code === NGAY_HACH_TOAN).data;
                    this.dateLockNew = postedDate ? moment(postedDate, DATE_FORMAT) : null;
                }
            }
        });
    }

    accept() {
        this.updateDateCloseBook.dateClosedBookOld = this.dateLockNow;
        this.updateDateCloseBook.dateClosedBook = this.dateLockNew;
        if (this.checkErr()) {
            this.boKhoaSoKyKeToanService.updateDateClosedBook(this.updateDateCloseBook).subscribe(
                (res: HttpResponse<boolean>) => {
                    if (res.body) {
                        this.onSuccess();
                    } else {
                        this.error();
                    }
                },
                (res: HttpErrorResponse) => {
                    this.messageErr(res.message);
                    this.activeModal.close();
                }
            );
        }
    }

    private onSuccess() {
        this.toastr.success(
            this.translate.instant('ebwebApp.unlockBook.unlockSuccess'),
            this.translate.instant('ebwebApp.mCReceipt.home.message')
        );
        this.principal.identity(true).then(account => {
            this.eventManager.broadcast({
                name: 'unlockSuccess'
            });
        });
        this.activeModal.close();
    }

    private error() {
        this.toastr.error(
            this.translate.instant('ebwebApp.unlockBook.unlockErr'),
            this.translate.instant('ebwebApp.mCReceipt.home.message')
        );
        this.activeModal.close();
    }

    private messageErr(message) {
        this.toastr.error(message, this.translate.instant('ebwebApp.mCReceipt.home.message'));
    }

    checkErr() {
        if (!this.dateLockNew) {
            this.messageErr(this.translate.instant('ebwebApp.unlockBook.dateNull'));
            return false;
        }
        if (this.dateLockNow && (this.dateLockNow.isBefore(this.dateLockNew) || this.dateLockNow.isSame(this.dateLockNew))) {
            this.messageErr(this.translate.instant('ebwebApp.unlockBook.err1'));
            return false;
        }
        return true;
    }
}
