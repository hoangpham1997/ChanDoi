import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { IAccountList } from 'app/shared/model/account-list.model';
import { Principal } from 'app/core';
import { ITEMS_PER_PAGE } from 'app/shared';
import { AccountListService } from './account-list.service';
import { IMBDeposit } from 'app/shared/model/mb-deposit.model';
import { TranslateService } from '@ngx-translate/core';
import { ITreeAccountList } from 'app/shared/model/account-list-tree.model';
import { content } from 'html2canvas/dist/types/css/property-descriptors/content';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ROLE } from 'app/role.constants';
import { TreeGridComponent } from 'app/shared/tree-grid/tree-grid.component';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';

@Component({
    selector: 'eb-account-list',
    templateUrl: './account-list.component.html',
    styleUrls: ['./account-list.component.css']
})
export class AccountListComponent extends BaseComponent implements OnInit, OnDestroy {
    currentAccount: any;
    accountListsNotChange: IAccountList[];
    accountLists: IAccountList[];
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
    accountNumber: string;
    selectedRow: ITreeAccountList;
    treeAccountList: ITreeAccountList[];
    listParentAccount: ITreeAccountList[];
    flatTreeAccountList: ITreeAccountList[];
    listTHead: string[];
    listKey: any[];
    listSearch: any;
    navigateForm: string;
    keySearch: any[];
    index: number;
    @ViewChild('child') child: TreeGridComponent;
    ROLE_HeThongTaiKhoan_Them = ROLE.HeThongTaiKhoan_Them;
    ROLE_HeThongTaiKhoan_Xem = ROLE.HeThongTaiKhoan_Xem;
    ROLE_HeThongTaiKhoan_Sua = ROLE.HeThongTaiKhoan_Sua;
    ROLE_HeThongTaiKhoan_Xoa = ROLE.HeThongTaiKhoan_Xoa;

    buttonDeleteTranslate = 'ebwebApp.mBDeposit.toolTip.delete';
    buttonAddTranslate = 'ebwebApp.mBDeposit.toolTip.add';
    buttonEditTranslate = 'ebwebApp.mBDeposit.toolTip.edit';
    buttonRecordTranslate = 'ebwebApp.mBDeposit.toolTip.record';
    buttonUnRecordTranslate = 'ebwebApp.mBDeposit.toolTip.unrecord';
    buttonExportTranslate = 'ebwebApp.mBDeposit.toolTip.export';
    buttonSearchTranslate = 'ebwebApp.mBDeposit.toolTip.search';

    constructor(
        private accountListService: AccountListService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        private translate: TranslateService
    ) {
        super();
        this.itemsPerPage = 20;
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });
    }

    loadAll() {
        // let index = 0;
        this.accountListService.getAccountLists().subscribe((res: HttpResponse<IAccountList[]>) => {
            this.index = 0;
            this.listParentAccount = [];
            this.flatTreeAccountList = [];
            this.accountListsNotChange = res.body;
            for (let i = 0; i < this.accountListsNotChange.length; i++) {
                if (!this.accountListsNotChange[i].accountNumber) {
                    this.accountListsNotChange[i].accountNumber = '';
                }
                if (!this.accountListsNotChange[i].accountName) {
                    this.accountListsNotChange[i].accountName = '';
                }
                if (!this.accountListsNotChange[i].accountNameGlobal) {
                    this.accountListsNotChange[i].accountNameGlobal = '';
                }
                if (
                    this.accountListsNotChange[i].accountGroupKind !== null ||
                    this.accountListsNotChange[i].accountGroupKind !== undefined
                ) {
                    this.getAccountGroupKind(this.accountListsNotChange[i].accountGroupKind, i);
                }
            }
            this.accountLists = res.body;
            this.selectedRow = this.accountLists[0];
            const listAccount = this.accountLists.filter(a => a.grade === 1);
            for (let i = 0; i < listAccount.length; i++) {
                this.listParentAccount.push(Object.assign({}));
                this.listParentAccount[i].parent = listAccount[i];
            }
            this.tree(this.listParentAccount, 1);
            this.setIndexTree(this.listParentAccount, 1);
            this.cutTree(this.listParentAccount);
            this.objects = this.flatTreeAccountList;
            this.selectedRow = this.flatTreeAccountList[0];
        });
    }

    registerSelectedRow() {
        this.eventSubscriber = this.eventManager.subscribe('selectRow', response => {
            this.selectedRow = response.data;
        });
    }

    registerSelectedRows() {
        this.eventSubscriber = this.eventManager.subscribe('selectRows', response => {
            this.selectedRows = response.data;
            console.log(response.data);
        });
    }

    treeforSearch() {
        for (let i = 0; i < this.accountLists.length; i++) {
            if (this.accountLists[i].parentAccountID) {
                this.getAllParent(this.accountLists[i]);
            }
        }
    }

    getAllParent(accountList: IAccountList) {
        const addAccount = this.accountListsNotChange.find(a => a.id === accountList.parentAccountID);
        const checkExistAccount = this.accountLists.find(a => a.id === addAccount.id);
        if (addAccount && !checkExistAccount) {
            this.accountLists.push(addAccount);
            if (addAccount.parentAccountID) {
                this.getAllParent(addAccount);
            }
        }
    }

    tree(accountList: ITreeAccountList[], grade) {
        for (let i = 0; i < accountList.length; i++) {
            const newList = this.accountLists.filter(a => a.parentAccountID === accountList[i].parent.id);
            accountList[i].children = [];
            for (let j = 0; j < newList.length; j++) {
                // if (j === 0) {
                //     accountList[i].children = [];
                // }
                accountList[i].children.push(Object.assign({}));
                accountList[i].children[j].parent = newList[j];
            }
            if (accountList[i].children && accountList[i].children.length > 0) {
                this.tree(accountList[i].children, grade + 1);
            }
        }
        // for (let i = 0; i < accountList.length; i++) {
        //     if (accountList[i].parent.accountGroupKind !== null || accountList[i].parent.accountGroupKind !== undefined) {
        //         accountList[i].parent.getAccountGroupKind = this.getAccountGroupKind(accountList[i].parent.accountGroupKind);
        //     }
        // }
    }

    cutTree(tree: ITreeAccountList[]) {
        tree.forEach(branch => {
            this.flatTreeAccountList.push(branch);
            if (branch.children && branch.children.length > 0) {
                this.cutTree(branch.children);
            }
        });
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    transition() {
        this.router.navigate(['/account-list'], {
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
            '/account-list',
            {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.loadAll();
    }

    ngOnInit() {
        this.treeAccountList = [];
        this.accountLists = [];
        this.accountListsNotChange = [];
        this.listTHead = [];
        this.listKey = [];
        this.keySearch = [];
        this.listSearch = {};
        this.index = 0;
        this.navigateForm = './account-list';
        this.listTHead.push('ebwebApp.accountList.accountNumber');
        this.listTHead.push('ebwebApp.accountList.accountName');
        this.listTHead.push('ebwebApp.accountList.accountNameGlobal');
        this.listTHead.push('ebwebApp.accountList.accountGroupKind');
        this.listTHead.push('ebwebApp.accountList.isActive');
        this.listKey.push({ key: 'accountNumber', type: 1 });
        this.listKey.push({ key: 'accountName', type: 1 });
        this.listKey.push({ key: 'accountNameGlobal', type: 1 });
        this.listKey.push({ key: 'getAccountGroupKind', type: 2 });
        this.listKey.push({ key: 'isActive', type: 1 });
        this.listSearch = {
            accountNumber: '',
            accountName: '',
            accountNameGlobal: '',
            getAccountGroupKind: '',
            isActive: true
        };
        this.keySearch.push('accountNumber');
        this.keySearch.push('accountName');
        this.keySearch.push('accountNameGlobal');
        this.keySearch.push('getAccountGroupKind');
        this.keySearch.push('isActive');
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInAccountLists();
        this.onSelect(this.selectedRow);
        this.registerSelectedRow();
        this.registerSelectedRows();
        this.registerChangeListSearch();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IAccountList) {
        return item.id;
    }

    registerChangeInAccountLists() {
        this.eventSubscriber = this.eventManager.subscribe('accountListListModification', response => this.loadAll());
    }

    registerChangeListSearch() {
        this.eventSubscriber = this.eventManager.subscribe('listSearch', response => {
            this.listSearch = response.data;
            this.listParentAccount = [];
            this.flatTreeAccountList = [];
            this.accountLists = this.accountListsNotChange.filter(
                a =>
                    a.accountNumber.includes(this.listSearch.accountNumber) &&
                    a.accountName.toUpperCase().includes(this.listSearch.accountName.toUpperCase()) &&
                    a.accountNameGlobal.toUpperCase().includes(this.listSearch.accountNameGlobal.toUpperCase()) &&
                    a.getAccountGroupKind.toUpperCase().includes(this.listSearch.getAccountGroupKind.toUpperCase()) &&
                    a.isActive === this.listSearch.isActive
            );
            this.treeforSearch();
            let grade;
            if (this.accountLists.length) {
                grade = this.accountLists[0].grade;
            }
            for (let i = 0; i < this.accountLists.length; i++) {
                if (this.accountLists[i].grade < grade) {
                    grade = this.accountLists[i].grade;
                }
            }
            const listAccount = this.accountLists.filter(a => a.grade === grade);
            for (let i = 0; i < listAccount.length; i++) {
                this.listParentAccount.push(Object.assign({}));
                this.listParentAccount[i].parent = listAccount[i];
            }
            this.tree(this.listParentAccount, grade);
            this.setIndexTree(this.listParentAccount, grade);
            this.cutTree(this.listParentAccount);
        });
    }

    setIndexTree(accountList: ITreeAccountList[], grade) {
        for (let i = 0; i < accountList.length; i++) {
            accountList[i].index = this.index;
            this.index++;
            if (accountList[i].children && accountList[i].children.length > 0) {
                this.setIndexTree(accountList[i].children, grade + 1);
            }
        }
    }

    sort() {
        const result = ['accountNumber' + ',' + (this.reverse ? 'asc' : 'desc')];
        return result;
    }

    private paginateAccountLists(data: IAccountList[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.accountLists = data;
        this.itemsPerPage = this.totalItems;
        this.selectedRow = this.accountLists[0];
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    selectedItemPerPage() {
        this.loadAll();
    }

    getAccountGroupKind(accountGroupKind: number, i) {
        this.translate
            .get([
                'ebwebApp.accountList.debit',
                'ebwebApp.accountList.credit',
                'ebwebApp.accountList.hermaphrodite',
                'ebwebApp.accountList.noBalance'
            ])
            .subscribe(res2 => {
                if (accountGroupKind === 0) {
                    this.accountListsNotChange[i].getAccountGroupKind = res2['ebwebApp.accountList.debit'];
                } else if (accountGroupKind === 1) {
                    this.accountListsNotChange[i].getAccountGroupKind = res2['ebwebApp.accountList.credit'];
                } else if (accountGroupKind === 2) {
                    this.accountListsNotChange[i].getAccountGroupKind = res2['ebwebApp.accountList.hermaphrodite'];
                } else if (accountGroupKind === 3) {
                    this.accountListsNotChange[i].getAccountGroupKind = res2['ebwebApp.accountList.noBalance'];
                } else {
                    this.accountListsNotChange[i].getAccountGroupKind = '';
                }
            });
    }

    // onSelect(select: IAccountList) {
    //     this.selectedRow = select;
    // }
    onSelect(select: ITreeAccountList, evt?) {
        this.child.onSelect(select, evt);
    }

    @ebAuth(['ROLE_ADMIN', ROLE.HeThongTaiKhoan_Xem])
    edit() {
        event.preventDefault();
        if (this.selectedRow.parent.id) {
            this.router.navigate(['./account-list', this.selectedRow.parent.id, 'edit']);
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.HeThongTaiKhoan_Xoa])
    delete() {
        event.preventDefault();
        if (this.selectedRows.length > 1) {
        } else {
            if (this.selectedRow.parent.id) {
                this.router.navigate(['/account-list', { outlets: { popup: this.selectedRow.parent.id + '/delete' } }]);
            }
        }
    }

    newArr(lenght: number): any[] {
        if (lenght > 0) {
            return new Array(lenght);
        } else {
            return new Array(0);
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.HeThongTaiKhoan_Them])
    addNew(event) {
        event.preventDefault();
        this.router.navigate(['./account-list', 'new']);
    }
}
