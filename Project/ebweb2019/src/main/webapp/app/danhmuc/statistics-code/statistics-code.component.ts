import { Component, OnInit, OnDestroy, AfterViewInit, ViewChild } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';
import { Principal } from 'app/core';
import { StatisticsCodeService } from './statistics-code.service';
import { IStatisticsCode } from 'app/shared/model/statistics-code.model';
import { ITreeStatisticsCode } from 'app/shared/model/statistics-code-tree.model';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ROLE } from 'app/role.constants';
import { TreeGridComponent, TreeGridItemComponent } from 'app/shared/tree-grid/tree-grid.component';

@Component({
    selector: 'eb-statistics-code',
    templateUrl: './statistics-code.component.html',
    styleUrls: ['./statistics-code.component.css']
})
export class StatisticsCodeComponent extends BaseComponent implements OnInit, OnDestroy {
    currentAccount: any;
    statisticsCodes: IStatisticsCode[];
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
    selectedRow: ITreeStatisticsCode;
    treeStatisticsCode: ITreeStatisticsCode[];
    listParentStatisticsCode: ITreeStatisticsCode[];
    flatTreeStatisticsCode: ITreeStatisticsCode[];
    listHead: string[];
    listKey: any[];
    navigateForm: string;
    ROLE_MTK_Them = ROLE.DanhMucMaThongKe_Them;
    ROLE_MTK_Sua = ROLE.DanhMucMaThongKe_Sua;
    ROLE_MTK_Xoa = ROLE.DanhMucMaThongKe_Xoa;
    @ViewChild('child') child: TreeGridComponent;

    constructor(
        private statisticsCodeService: StatisticsCodeService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager
    ) {
        super();
    }

    loadAll() {
        this.statisticsCodeService.getStatisticsCodes().subscribe((res: HttpResponse<IStatisticsCode[]>) => {
            this.listParentStatisticsCode = [];
            this.flatTreeStatisticsCode = [];
            this.statisticsCodes = res.body;
            const listStatisticsCode = this.statisticsCodes.filter(s => s.parentID === null || s.parentID === undefined);
            for (let i = 0; i < listStatisticsCode.length; i++) {
                this.listParentStatisticsCode.push(Object.assign({}));
                this.listParentStatisticsCode[i].parent = listStatisticsCode[i];
            }
            this.tree(this.listParentStatisticsCode);
            this.cutTree(this.listParentStatisticsCode);
            this.objects = this.flatTreeStatisticsCode;
            this.selectedRow = this.flatTreeStatisticsCode[0];
        });
    }

    tree(statisticsCode: ITreeStatisticsCode[]) {
        for (let i = 0; i < statisticsCode.length; i++) {
            const newList = this.statisticsCodes.filter(s => s.parentID === statisticsCode[i].parent.id);
            statisticsCode[i].children = [];
            for (let j = 0; j < newList.length; j++) {
                statisticsCode[i].children.push(Object.assign({}));
                statisticsCode[i].children[j].parent = newList[j];
            }
            if (statisticsCode[i].children && statisticsCode[i].children.length > 0) {
                this.tree(statisticsCode[i].children);
            }
        }
    }

    cutTree(tree: ITreeStatisticsCode[]) {
        tree.forEach(branch => {
            this.flatTreeStatisticsCode.push(branch);
            if (branch.children && branch.children.length > 0) {
                this.cutTree(branch.children);
            }
        });
    }

    transition() {
        this.router.navigate(['/statistics-code'], {
            queryParams: {
                page: this.page,
                size: this.itemsPerPage,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
    }

    clear() {
        this.page = 0;
        this.router.navigate([
            '/statistics-code',
            {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.loadAll();
    }

    ngOnInit() {
        this.statisticsCodes = [];
        this.treeStatisticsCode = [];
        this.listHead = [];
        this.listKey = [];
        this.navigateForm = './statistics-code';
        this.listHead.push('ebwebApp.statisticsCode.statisticsCode');
        this.listHead.push('ebwebApp.statisticsCode.statisticsCodeName');
        this.listHead.push('ebwebApp.statisticsCode.description');
        this.listHead.push('ebwebApp.statisticsCode.isActive');
        this.listKey.push({ key: 'statisticsCode', type: 1 });
        this.listKey.push({ key: 'statisticsCodeName', type: 1 });
        this.listKey.push({ key: 'description', type: 1 });
        this.listKey.push({ key: 'isActive', type: 1 });
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.onSelect(this.selectedRow);
        this.registerChangeInStatisticsCodes();
        this.registerSelectedRow();
    }

    registerSelectedRow() {
        this.eventSubscriber = this.eventManager.subscribe('selectRow', response => {
            this.selectedRow = response.data;
        });
    }

    onSelect(select: ITreeStatisticsCode) {
        this.child.onSelect(select);
    }

    ngOnDestroy() {
        if (this.eventSubscriber) {
            this.eventManager.destroy(this.eventSubscriber);
        }
    }

    trackId(index: number, item: IStatisticsCode) {
        return item.id;
    }

    registerChangeInStatisticsCodes() {
        this.eventSubscriber = this.eventManager.subscribe('statisticsCodesModification', response => this.loadAll());
    }

    addNew($event?) {
        event.preventDefault();
        this.router.navigate(['/statistics-code/new']);
    }

    edit() {
        event.preventDefault();
        if (this.selectedRow.parent.id) {
            this.router.navigate(['./statistics-code', this.selectedRow.parent.id, 'edit']);
        }
    }

    delete() {
        event.preventDefault();
        if (this.selectedRow.parent.id) {
            this.router.navigate(['/statistics-code', { outlets: { popup: this.selectedRow.parent.id + '/delete' } }]);
        }
    }

    newArr(length: number): any[] {
        if (length > 0) {
            return new Array(length);
        } else {
            return new Array(0);
        }
    }
}
