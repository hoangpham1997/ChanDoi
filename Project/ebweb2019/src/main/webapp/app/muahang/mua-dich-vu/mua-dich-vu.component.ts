import { Component, OnInit, ViewChild } from '@angular/core';
import { JhiLanguageHelper, Principal } from 'app/core';
import { ActivatedRoute, ActivatedRouteSnapshot, NavigationEnd, Router } from '@angular/router';
import { JhiEventManager } from 'ng-jhipster';
import { Subscription } from 'rxjs';
import { ONBROADCASTEVENT } from './mua-dich-vu-event-name.constant';
import { TranslateService } from '@ngx-translate/core';
import { ToastrService } from 'ngx-toastr';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { TabInsertUpdateReceiptMuaDichVuComponent } from 'app/muahang/mua-dich-vu/insert-update-mua-dich-vu/tab-insert-update-receipt-mua-dich-vu.component';
import { MuaDichVuService } from 'app/muahang/mua-dich-vu/mua-dich-vu.service';
import { SO_LAM_VIEC } from 'app/app.constants';
import { ISearchVoucher } from 'app/shared/model/SearchVoucher';
import { MuaDichVuResult } from 'app/muahang/mua-dich-vu/model/mua-dich-vu-result.model';
import { Location } from '@angular/common';

@Component({
    selector: 'eb-mua-dich-vu',
    templateUrl: './mua-dich-vu.component.html',
    styleUrls: ['mua-dich-vu.css']
})
export class MuaDichVuComponent extends BaseComponent implements OnInit {
    statusSearch: boolean;
    eventSubscriber: Subscription;
    viewType: number;
    searchVoucher: ISearchVoucher;
    @ViewChild(TabInsertUpdateReceiptMuaDichVuComponent) child: TabInsertUpdateReceiptMuaDichVuComponent;

    constructor(
        private jhiLanguageHelper: JhiLanguageHelper,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        private translateService: TranslateService,
        private toastrService: ToastrService,
        private muaDichVuService: MuaDichVuService,
        private principal: Principal,
        private location: Location
    ) {
        super();
    }

    ngOnInit(): void {
        this.activatedRoute.data.subscribe(data => {
            if (data.ppServiceId) {
                this.principal.identity().then(account => {
                    const currentBook = account.systemOption.find(x => x.code === SO_LAM_VIEC).data;
                    this.onShowDetailById(data.ppServiceId, currentBook, data.actionClose);
                });
            } else if (data.viewType) {
                this.viewType = data.viewType;
                const ppService = new MuaDichVuResult();
                ppService.reason = 'Mua dịch vụ';
                ppService.otherReason = ppService.reason;
                this.muaDichVuService.setPPServiceSelected(ppService);
                this.muaDichVuService.setReadOnly(false);
            } else {
                this.viewType = 0;
            }
        });
        this.router.events.subscribe(event => {
            if (event instanceof NavigationEnd) {
                this.jhiLanguageHelper.updateTitle(this.getPageTitle(this.router.routerState.snapshot.root));
            }
        });

        this.onUpdateStatusSearch();
        this.onUpdateViewType();
        this.onUpdateData();
    }

    onShowDetailById(ppServiceId: string, currentBook: string, actionClose: string) {
        this.muaDichVuService
            .findPPServiceByLocationItem({
                ppServiceId,
                currentBook: parseInt(currentBook, 0),
                ...this.muaDichVuService.getMuaDichVuSearchSnapShot()
            })
            .subscribe(res => {
                const ppService = res.body.result;
                this.muaDichVuService.setDataResult({
                    messages: res.body.messages,
                    totalRows: res.body.totalRows,
                    rowNum: res.body.rowNum
                });
                this.muaDichVuService.setPPServiceSelected(ppService);
                this.eventManager.broadcast({
                    name: ONBROADCASTEVENT.UPDATE_VIEW_TYPE_MUA_DICH_VU,
                    content: 1
                });
                this.muaDichVuService.setPPServiceSelected(ppService);
                this.muaDichVuService.setReadOnly(true);
                this.muaDichVuService.setIsFromOtherScene(true);

                this.muaDichVuService.setOnClose(() => {
                    switch (actionClose) {
                        case ONBROADCASTEVENT.selectViewVoucher:
                            this.router.navigate(['/mua-dich-vu']);
                            break;
                        case ONBROADCASTEVENT.fromPPService:
                            this.eventManager.broadcast({
                                name: ONBROADCASTEVENT.UPDATE_VIEW_TYPE_MUA_DICH_VU,
                                content: 0
                            });
                            this.location.go('/mua-dich-vu');
                            break;
                        case ONBROADCASTEVENT.fromMCPayment:
                            this.closeAllFromMCPayment();
                            break;
                        case ONBROADCASTEVENT.fromMBCreditCard:
                            this.closeAllFromMBCreditCard();
                            break;
                        case ONBROADCASTEVENT.fromMBTellerPaper:
                            this.closeAllFromMBTellerPaper();
                            break;
                        default:
                            this.router.navigate(['nhan-hoa-don']);
                    }
                });
            });
    }

    closeAllFromMCPayment() {
        this.searchVoucher = JSON.parse(sessionStorage.getItem('searchVoucherMCPayment'));
        if (this.searchVoucher) {
            if (sessionStorage.getItem('page_MCPayment')) {
                this.router.navigate(['/mc-payment', 'hasSearch', '1'], {
                    queryParams: {
                        page: sessionStorage.getItem('page_MCPayment'),
                        size: sessionStorage.getItem('size_MCPayment'),
                        index: sessionStorage.getItem('index_MCPayment')
                    }
                });
            } else {
                this.router.navigate(['/mc-payment', 'hasSearch', '1']);
            }
        } else {
            if (sessionStorage.getItem('page_MCPayment')) {
                this.router.navigate(['/mc-payment'], {
                    queryParams: {
                        page: sessionStorage.getItem('page_MCPayment'),
                        size: sessionStorage.getItem('size_MCPayment'),
                        index: sessionStorage.getItem('index_MCPayment')
                    }
                });
            } else {
                this.router.navigate(['/mc-payment']);
            }
        }
    }

    closeAllFromMBCreditCard() {
        this.searchVoucher = JSON.parse(sessionStorage.getItem('dataSearchMBCreditCard'));
        if (this.searchVoucher) {
            this.router.navigate(['/mb-credit-card', 'hasSearch', '1']);
        } else {
            this.router.navigate(['/mb-credit-card']);
        }
    }

    closeAllFromMBTellerPaper() {
        // this.searchVoucher = JSON.parse(sessionStorage.getItem('dataSearchMBCreditCard'));
        this.router.navigate(['/mb-teller-paper']);
    }

    onUpdateStatusSearch() {
        this.eventSubscriber = this.eventManager.subscribe(ONBROADCASTEVENT.UPDATE_SEARCH_STATUS_MUA_DICH_VU_STATUS, response => {
            this.statusSearch = response.content;
        });
    }

    onUpdateViewType() {
        this.eventSubscriber = this.eventManager.subscribe(ONBROADCASTEVENT.UPDATE_VIEW_TYPE_MUA_DICH_VU, response => {
            this.viewType = response.content;
        });
    }

    onUpdateData() {
        this.eventSubscriber = this.eventManager.subscribe(ONBROADCASTEVENT.DELETE_PP_SERVICE_SUCCESS, () => {
            this.toastrService.success(this.translateService.instant('ebwebApp.muaHang.muaDichVu.toastr.deletedSuccess'));
        });
    }

    private getPageTitle(routeSnapshot: ActivatedRouteSnapshot) {
        let title: string = routeSnapshot.data && routeSnapshot.data['pageTitle'] ? routeSnapshot.data['pageTitle'] : 'cicbApp';
        if (routeSnapshot.firstChild) {
            title = this.getPageTitle(routeSnapshot.firstChild) || title;
        }
        return title;
    }

    canDeactive() {
        if (this.child ? this.child.canDeactive() : true) {
            this.muaDichVuService.cleanService();
            return true;
        }
    }
}
