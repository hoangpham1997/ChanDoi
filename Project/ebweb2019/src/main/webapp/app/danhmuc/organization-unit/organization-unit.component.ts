import { Component, OnInit, OnDestroy, ViewEncapsulation } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';
import { IUser, Principal, UserService } from 'app/core';

import { ITEMS_PER_PAGE } from 'app/shared';
import { OrganizationUnitService } from './organization-unit.service';
import { IAccountList } from 'app/shared/model/account-list.model';
import { TranslateService } from '@ngx-translate/core';
import { ITreeAccountList } from 'app/shared/model/account-list-tree.model';
import { ITreeOrganizationUnit } from 'app/shared/model/organization-unit-tree.model';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ToastrService } from 'ngx-toastr';
import { ROLE } from 'app/role.constants';
import { EbPackageService } from 'app/admin';
import { IEbPackage } from 'app/shared/model/eb-package.model';
import { EbPackage } from 'app/app.constants';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';

@Component({
    selector: 'eb-organization-unit',
    templateUrl: './organization-unit.component.html',
    styleUrls: ['./organization-unit.component.css'],
    encapsulation: ViewEncapsulation.None
})
export class OrganizationUnitComponent extends BaseComponent implements OnInit, OnDestroy {
    currentAccount: any;
    organizationUnits: IOrganizationUnit[];
    error: any;
    success: any;
    eventSubscriber: Subscription;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    unitTypes: any[];
    isCreateCompany: number;
    selectedRow: ITreeOrganizationUnit;
    listParentOrganizationUnit: ITreeOrganizationUnit[];
    organizationUnitsNotChange: ITreeOrganizationUnit[];
    listTHead: string[];
    listKey: any[];
    listSearch: any;
    keySearch: any[];
    navigateForm: string;
    ROLE_CoCauToChuc_Xem = ROLE.CoCauToChuc_Xem;
    ROLE_CoCauToChuc_Them = ROLE.CoCauToChuc_Them;
    ROLE_CoCauToChuc_Sua = ROLE.CoCauToChuc_Sua;
    ROLE_CoCauToChuc_Xoa = ROLE.CoCauToChuc_Xoa;
    ROLE = ROLE;
    isUserID: string;
    index: number;
    orgLogin: string;

    buttonDeleteTranslate = 'ebwebApp.mBDeposit.toolTip.delete';
    buttonAddTranslate = 'ebwebApp.mBDeposit.toolTip.add';
    buttonEditTranslate = 'ebwebApp.mBDeposit.toolTip.edit';
    buttonRecordTranslate = 'ebwebApp.mBDeposit.toolTip.record';
    buttonUnRecordTranslate = 'ebwebApp.mBDeposit.toolTip.unrecord';
    buttonExportTranslate = 'ebwebApp.mBDeposit.toolTip.export';
    buttonSearchTranslate = 'ebwebApp.mBDeposit.toolTip.search';
    isAdminWithTotalPackage: number;
    currentUnitType: number;

    comType: number;
    userID: number;

    constructor(
        private organizationUnitService: OrganizationUnitService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        private translate: TranslateService,
        private userService: UserService,
        private ebPackageService: EbPackageService,
        private toastr: ToastrService
    ) {
        super();
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });
        this.principal.identity().then(account => {
            this.currentAccount = account;
            this.orgLogin = account.organizationUnit.id;
            this.comType = account.ebPackage.comType;
            this.userID = account.organizationUnit.userID;
            this.isAdminWithTotalPackage = account.ebPackage.isTotalPackage;
            this.isAdminWithTotalPackage = account.ebPackage.comType;
            this.currentUnitType = account.organizationUnit.unitType;
        });
    }

    loadAll() {
        // this.translate
        //     .get([
        //         'ebwebApp.organizationUnit.corporations',
        //         'ebwebApp.organizationUnit.branch',
        //         'ebwebApp.organizationUnit.representativeOffice',
        //         'ebwebApp.organizationUnit.placeOfBusiness',
        //         'ebwebApp.organizationUnit.department',
        //         'ebwebApp.organizationUnit.other'
        //     ])
        //     .subscribe(translate => {
        //         this.organizationUnitService.getOrganizationUnitsByCompanyID().subscribe((res: HttpResponse<IOrganizationUnit[]>) => {
        //             this.listParentOrganizationUnit = [];
        //             this.organizationUnits = res.body;
        //             for (let i = 0; i < this.organizationUnits.length; i++) {
        //                 if (this.organizationUnits[i].unitType !== null || this.organizationUnits[i].unitType !== undefined) {
        //                     if (this.organizationUnits[i].unitType === 0) {
        //                         this.organizationUnits[i].getUnitName = translate['ebwebApp.organizationUnit.corporations'];
        //                     } else if (this.organizationUnits[i].unitType === 1) {
        //                         this.organizationUnits[i].getUnitName = translate['ebwebApp.organizationUnit.branch'];
        //                     } else if (this.organizationUnits[i].unitType === 2) {
        //                         this.organizationUnits[i].getUnitName = translate['ebwebApp.organizationUnit.representativeOffice'];
        //                     } else if (this.organizationUnits[i].unitType === 3) {
        //                         this.organizationUnits[i].getUnitName = translate['ebwebApp.organizationUnit.placeOfBusiness'];
        //                     } else if (this.organizationUnits[i].unitType === 4) {
        //                         this.organizationUnits[i].getUnitName = translate['ebwebApp.organizationUnit.department'];
        //                     } else if (this.organizationUnits[i].unitType === 5) {
        //                         this.organizationUnits[i].getUnitName = translate['ebwebApp.organizationUnit.other'];
        //                     }
        //                 }
        //                 if (this.organizationUnits[i].parentID !== null || this.organizationUnits[i].parentID !== undefined) {
        //                     this.organizationUnits[i].getParentName = this.getOrganizationUnitName(this.organizationUnits[i].parentID);
        //                 }
        //             }
        //             let index = 0;
        //             const listOrganizationUnit = this.organizationUnits.filter(a => a.grade === 1);
        //             for (let i = 0; i < listOrganizationUnit.length; i++) {
        //                 this.listParentOrganizationUnit.push(Object.assign({}));
        //                 this.listParentOrganizationUnit[index].parent = listOrganizationUnit[i];
        //                 index++;
        //             }
        //             this.tree(this.listParentOrganizationUnit, 1);
        //         });
        //     });
        if (this.isAdminWithTotalPackage === 3 && !this.userID && this.currentUnitType === 0) {
            this.organizationUnitService.getTreeAllOrganizationUnitsByOfUserId().subscribe((res: HttpResponse<ITreeOrganizationUnit[]>) => {
                this.listParentOrganizationUnit = res.body;
                this.organizationUnitsNotChange = res.body;
                this.setIndexTree(this.listParentOrganizationUnit);
                this.organizationUnitService.recursiveOrganizationUnitByParentID().subscribe((res2: HttpResponse<IOrganizationUnit[]>) => {
                    this.organizationUnits = res2.body;
                    this.getUnitNameAndParentNameTree(this.listParentOrganizationUnit);
                    this.getUnitNameAndParentName(this.organizationUnits);
                });
            });
        } else {
            this.organizationUnitService.getTreeOrganizationUnits().subscribe((res: HttpResponse<ITreeOrganizationUnit[]>) => {
                this.listParentOrganizationUnit = res.body;
                this.organizationUnitsNotChange = res.body;
                this.setIndexTree(this.listParentOrganizationUnit);
                this.organizationUnitService.recursiveOrganizationUnitByParentID().subscribe((res2: HttpResponse<IOrganizationUnit[]>) => {
                    this.organizationUnits = res2.body;
                    console.log(this.organizationUnits);
                    this.getUnitNameAndParentNameTree(this.listParentOrganizationUnit);
                    this.getUnitNameAndParentName(this.organizationUnits);
                });
            });
        }
    }

    getUnitNameAndParentNameTree(listOrg: ITreeOrganizationUnit[]) {
        this.translate
            .get([
                'ebwebApp.organizationUnit.corporations',
                'ebwebApp.organizationUnit.branch',
                'ebwebApp.organizationUnit.representativeOffice',
                'ebwebApp.organizationUnit.placeOfBusiness',
                'ebwebApp.organizationUnit.department',
                'ebwebApp.organizationUnit.other'
            ])
            .subscribe(translate => {
                for (let i = 0; i < listOrg.length; i++) {
                    if (listOrg[i].parent.unitType !== null || listOrg[i].parent.unitType !== undefined) {
                        if (listOrg[i].parent.unitType === 0) {
                            listOrg[i].parent.getUnitName = translate['ebwebApp.organizationUnit.corporations'];
                        } else if (listOrg[i].parent.unitType === 1) {
                            listOrg[i].parent.getUnitName = translate['ebwebApp.organizationUnit.branch'];
                        } else if (listOrg[i].parent.unitType === 2) {
                            listOrg[i].parent.getUnitName = translate['ebwebApp.organizationUnit.representativeOffice'];
                        } else if (listOrg[i].parent.unitType === 3) {
                            listOrg[i].parent.getUnitName = translate['ebwebApp.organizationUnit.placeOfBusiness'];
                        } else if (listOrg[i].parent.unitType === 4) {
                            listOrg[i].parent.getUnitName = translate['ebwebApp.organizationUnit.department'];
                        } else if (listOrg[i].parent.unitType === 5) {
                            listOrg[i].parent.getUnitName = translate['ebwebApp.organizationUnit.other'];
                        }
                    }
                    if (listOrg[i].parent.parentID !== null || listOrg[i].parent.parentID !== undefined) {
                        listOrg[i].parent.getParentName = this.getOrganizationUnitName(listOrg[i].parent.parentID);
                    }
                    if (listOrg[i].children && listOrg[i].children.length > 0) {
                        this.getUnitNameAndParentNameTree(listOrg[i].children);
                    }
                }
            });
    }

    getUnitNameAndParentName(listOrg: IOrganizationUnit[]) {
        this.translate
            .get([
                'ebwebApp.organizationUnit.corporations',
                'ebwebApp.organizationUnit.branch',
                'ebwebApp.organizationUnit.representativeOffice',
                'ebwebApp.organizationUnit.placeOfBusiness',
                'ebwebApp.organizationUnit.department',
                'ebwebApp.organizationUnit.other'
            ])
            .subscribe(translate => {
                for (let i = 0; i < listOrg.length; i++) {
                    if (listOrg[i].unitType !== null || listOrg[i].unitType !== undefined) {
                        if (listOrg[i].unitType === 0) {
                            listOrg[i].getUnitName = translate['ebwebApp.organizationUnit.corporations'];
                        } else if (listOrg[i].unitType === 1) {
                            listOrg[i].getUnitName = translate['ebwebApp.organizationUnit.branch'];
                        } else if (listOrg[i].unitType === 2) {
                            listOrg[i].getUnitName = translate['ebwebApp.organizationUnit.representativeOffice'];
                        } else if (listOrg[i].unitType === 3) {
                            listOrg[i].getUnitName = translate['ebwebApp.organizationUnit.placeOfBusiness'];
                        } else if (listOrg[i].unitType === 4) {
                            listOrg[i].getUnitName = translate['ebwebApp.organizationUnit.department'];
                        } else if (listOrg[i].unitType === 5) {
                            listOrg[i].getUnitName = translate['ebwebApp.organizationUnit.other'];
                        }
                    }
                    if (listOrg[i].parentID !== null || listOrg[i].parentID !== undefined) {
                        listOrg[i].getParentName = this.getOrganizationUnitName(listOrg[i].parentID);
                    }
                }
            });
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    registerSelectedRow() {
        this.eventSubscriber = this.eventManager.subscribe('selectRow', response => {
            this.selectedRow = response.data;
        });
    }

    transition() {
        this.router.navigate(['/organization-unit'], {
            queryParams: {
                page: this.page,
                size: this.itemsPerPage,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll();
    }

    clear() {
        this.page = 0;
        this.router.navigate([
            '/organization-unit',
            {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.loadAll();
    }

    ngOnInit() {
        this.navigateForm = './organization-unit';
        this.listTHead = [];
        this.listKey = [];
        this.keySearch = [];
        this.listSearch = {};
        this.listTHead.push('ebwebApp.organizationUnit.organizationUnitCode');
        this.listTHead.push('ebwebApp.organizationUnit.organizationUnitName');
        this.listTHead.push('ebwebApp.organizationUnit.unitType');
        // this.listTHead.push('ebwebApp.organizationUnit.parentID');
        this.listTHead.push('ebwebApp.organizationUnit.isActive');
        this.listKey.push({ key: 'organizationUnitCode', type: 1 });
        this.listKey.push({ key: 'organizationUnitName', type: 1 });
        this.listKey.push({ key: 'getUnitName', type: 1 });
        // this.listKey.push({ key: 'getParentName', type: 2 });
        this.listKey.push({ key: 'isActive', type: 1 });
        this.listSearch = {
            organizationUnitCode: '',
            organizationUnitName: '',
            getUnitName: '',
            isActive: true
        };
        this.keySearch.push('organizationUnitCode');
        this.keySearch.push('organizationUnitName');
        this.keySearch.push('getUnitName');
        this.keySearch.push('isActive');
        this.loadAll();
        this.registerChangeInOrganizationUnits();
        this.registerChangeListSearch();
        this.unitTypes = ['Tổng cty/cty', 'Chi nhánh', 'Văn phòng đại diện', 'Địa điểm kinh doanh', 'Phòng ban', 'Khác'];
        this.registerSelectedRow();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IOrganizationUnit) {
        return item.id;
    }

    registerChangeInOrganizationUnits() {
        this.eventSubscriber = this.eventManager.subscribe('organizationUnitListModification', response => this.loadAll());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private paginateOrganizationUnits(data: IOrganizationUnit[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.organizationUnits = data;
    }

    onSelect(select: ITreeOrganizationUnit) {
        this.selectedRow = select;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    @ebAuth(['ROLE_ADMIN', ROLE.CoCauToChuc_Xem])
    edit() {
        event.preventDefault();
        if (this.selectedRow.parent.id) {
            this.router.navigate(['./organization-unit', this.selectedRow.parent.id, 'edit']);
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.CoCauToChuc_Xoa])
    delete() {
        event.preventDefault();
        if (this.selectedRow.parent.id !== this.orgLogin) {
            if (
                this.selectedRow.parent.id &&
                (this.selectedRow.parent.unitType !== 0 || (this.selectedRow.parent.unitType === 0 && this.comType === 3 && !this.userID))
            ) {
                this.router.navigate(['/organization-unit', { outlets: { popup: this.selectedRow.parent.id + '/delete' } }]);
            } else {
                this.toastr.warning(this.translate.instant('ebwebApp.organizationUnit.warningDeleteCoporation'));
            }
        } else {
            this.toastr.error(this.translate.instant('ebwebApp.organizationUnit.errorDeleteOrgLogin'));
        }
    }

    getOrganizationUnitName(organizationUnitID) {
        if (organizationUnitID) {
            const organizationUnit = this.organizationUnits.find(a => a.id === organizationUnitID);
            if (organizationUnit) {
                return organizationUnit.organizationUnitCode;
            }
        }
    }

    getUnitType(unitType) {
        if (unitType === 0) {
            return this.translate.instant('ebwebApp.organizationUnit.corporations');
        }
        if (unitType === 1) {
            return this.translate.instant('ebwebApp.organizationUnit.branch');
        }
        if (unitType === 2) {
            return this.translate.instant('ebwebApp.organizationUnit.representativeOffice');
        }
        if (unitType === 3) {
            return this.translate.instant('ebwebApp.organizationUnit.placeOfBusiness');
        }
        if (unitType === 4) {
            return this.translate.instant('ebwebApp.organizationUnit.department');
        }
        if (unitType === 5) {
            return this.translate.instant('ebwebApp.organizationUnit.other');
        }
    }

    tree(organizationUnits: ITreeOrganizationUnit[], grade) {
        for (let i = 0; i < organizationUnits.length; i++) {
            const newList = this.organizationUnits.filter(a => a.parentID === organizationUnits[i].parent.id);
            for (let j = 0; j < newList.length; j++) {
                if (j === 0) {
                    organizationUnits[i].children = [];
                }
                organizationUnits[i].children.push(Object.assign({}));
                organizationUnits[i].children[j].parent = newList[j];
            }
            if (organizationUnits[i].children && organizationUnits[i].children.length > 0) {
                this.tree(organizationUnits[i].children, grade + 1);
            }
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.ROLE_MGT, ROLE.CoCauToChuc_Them])
    addNew(event) {
        event.preventDefault();
        this.router.navigate(['./organization-unit', 'new']);
    }

    registerChangeListSearch() {
        this.eventSubscriber = this.eventManager.subscribe('listSearch', response => {
            this.listSearch = response.data;
            this.listParentOrganizationUnit = [];
            this.treeForSearch(this.organizationUnits);
            this.findAllParent(this.listParentOrganizationUnit);
            let listSort = this.listParentOrganizationUnit.map(object => ({ ...object }));
            this.sortTree(
                listSort.sort((n1, n2) => {
                    if (n1.parent.unitType > n2.parent.unitType) {
                        return -1;
                    }
                    if (n1.parent.unitType < n2.parent.unitType) {
                        return 1;
                    }
                    return 0;
                })
            );
        });
    }

    treeForSearch(organizationUnits) {
        for (let i = 0; i < organizationUnits.length; i++) {
            if (
                organizationUnits[i].organizationUnitCode.toUpperCase().includes(this.listSearch.organizationUnitCode.toUpperCase()) &&
                organizationUnits[i].organizationUnitName.toUpperCase().includes(this.listSearch.organizationUnitName.toUpperCase()) &&
                organizationUnits[i].getUnitName.toUpperCase().includes(this.listSearch.getUnitName.toUpperCase()) &&
                organizationUnits[i].isActive === this.listSearch.isActive
            ) {
                this.listParentOrganizationUnit.push({ parent: organizationUnits[i], children: [] });
            }
        }
    }

    findAllParent(listParentOrganizationUnit) {
        if (listParentOrganizationUnit && listParentOrganizationUnit.length > 0) {
            for (let i = 0; i < listParentOrganizationUnit.length; i++) {
                if (listParentOrganizationUnit[i].parent.parentID) {
                    const orgParent = this.organizationUnits.find(a => a.id === listParentOrganizationUnit[i].parent.parentID);
                    if (orgParent) {
                        const findOrgParent = listParentOrganizationUnit.find(a => a.parent.id === orgParent.id);
                        if (!findOrgParent) {
                            listParentOrganizationUnit.push({
                                parent: orgParent,
                                children: []
                            });
                        }
                        if (orgParent.parentID) {
                            let list: ITreeOrganizationUnit[] = [];
                            list.push({ parent: orgParent, children: [] });
                            this.findAllParent(list);
                        }
                    }
                }
            }
        }
    }

    sortTree(listSort) {
        if (listSort && listSort.length > 0) {
            for (let i = 0; i < listSort.length; i++) {
                if (listSort[i].parent.parentID) {
                    const temp = this.listParentOrganizationUnit.find(a => a.parent.id === listSort[i].parent.parentID);
                    if (temp) {
                        let index = this.listParentOrganizationUnit.indexOf(temp);
                        this.listParentOrganizationUnit[index].children.push(listSort[i]);
                        const temp2 = this.listParentOrganizationUnit.find(a => a.parent.id === listSort[i].parent.id);
                        let index2 = this.listParentOrganizationUnit.indexOf(temp2);
                        this.listParentOrganizationUnit.splice(index2, 1);
                    }
                }
            }
        }
    }

    setIndexTree(listParentOrganizationUnit: ITreeOrganizationUnit[]) {
        for (let i = 0; i < listParentOrganizationUnit.length; i++) {
            listParentOrganizationUnit[i].index = this.index;
            this.index++;
            if (listParentOrganizationUnit[i].children && listParentOrganizationUnit[i].children.length > 0) {
                this.setIndexTree(listParentOrganizationUnit[i].children);
            }
        }
    }
}
