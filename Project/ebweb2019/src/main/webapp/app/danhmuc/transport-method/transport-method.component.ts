import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';
import { Principal } from 'app/core';

import { TransportMethodService } from './transport-method.service';
import { TranslateService } from '@ngx-translate/core';
import { ITransportMethod } from 'app/shared/model/transport-method.model';
import { ToastrService } from 'ngx-toastr';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ROLE } from 'app/role.constants';

@Component({
    selector: 'eb-transport-method',
    templateUrl: './transport-method.component.html',
    styleUrls: ['./transport-method.component.css']
})
export class TransportMethodComponent extends BaseComponent implements OnInit, OnDestroy {
    transportMethods: ITransportMethod[];
    selectedRow: ITransportMethod;
    eventSucriber: Subscription;
    itemsPerPage: number;
    page: number;
    previousPage: number;
    totalItems: number;
    predicate: any;
    reverse: any;
    ROLE_TransportMethod_Them = ROLE.DanhMucPhuongThucVanChuyen_Them;
    ROLE_TransportMethod_Sua = ROLE.DanhMucPhuongThucVanChuyen_Sua;
    ROLE_TransportMethod_Xoa = ROLE.DanhMucPhuongThucVanChuyen_Xoa;
    constructor(
        private transportMethodService: TransportMethodService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private toastr: ToastrService,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        private translate: TranslateService
    ) {
        super();
    }

    ngOnInit() {
        this.transportMethods = [];
        this.itemsPerPage = 20;
        this.activatedRoute.data.subscribe(data => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.predicate = data.pagingParams.predicate;
            this.reverse = data.pagingParams.ascending;
        });
        this.loadAll();
        this.registerChangeITransportMethod();
    }

    newArr(lenght: number): any[] {
        if (lenght > 0) {
            return new Array(lenght);
        } else {
            return new Array(0);
        }
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    loadAll() {
        this.transportMethodService
            .getAll({
                page: this.page - 1,
                size: this.itemsPerPage
            })
            .subscribe(res => {
                this.transportMethods = res.body;
                this.objects = this.transportMethods;
                this.selectedRow = this.transportMethods[0];
                this.selectedRows.push(this.selectedRow);
                this.totalItems = parseInt(res.headers.get('X-Total-Count'), 10);
            });
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }
    addNew($event?) {
        event.preventDefault();
        this.router.navigate(['./transport-method', 'new']);
    }
    transition() {
        this.router.navigate(['/transport-method'], {
            queryParams: {
                page: this.page,
                size: this.itemsPerPage
            }
        });
        this.loadAll();
    }

    edit() {
        event.preventDefault();
        if (this.selectedRow.id) {
            this.router.navigate(['./transport-method', this.selectedRow.id, 'edit']);
        }
    }
    onSelect(sel: ITransportMethod) {
        this.selectedRow = sel;
    }
    delete() {
        event.preventDefault();
        if (this.selectedRow.id) {
            this.router.navigate(['/transport-method', { outlets: { popup: this.selectedRow.id + '/delete' } }]);
        }
    }
    registerChangeITransportMethod() {
        this.eventSucriber = this.eventManager.subscribe('TransportMethod', even => this.loadAll());
    }
    ngOnDestroy() {
        this.eventManager.destroy(this.eventSucriber);
    }
}
