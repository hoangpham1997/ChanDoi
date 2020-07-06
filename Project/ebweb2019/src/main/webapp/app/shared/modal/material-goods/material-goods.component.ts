import { Component, ElementRef, OnInit, Renderer } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';
import { JhiEventManager } from 'ng-jhipster';
import { LoginService } from 'app/core/login/login.service';
import { StateStorageService } from 'app/core/auth/state-storage.service';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { HttpResponse } from '@angular/common/http';
import { TranslateService } from '@ngx-translate/core';
import { IMaterialGoods } from '../../model/material-goods.model';
import { MaterialGoodsService } from 'app/danhmuc/material-goods';
import { RepositoryLedgerService } from 'app/entities/repository-ledger';
import { MATERIAL_GOODS_TYPE } from 'app/app.constants';
import { Principal } from 'app/core';

@Component({
    selector: 'eb-material-goods-modal',
    templateUrl: './material-goods.component.html',
    styleUrls: ['./material-goods.component.css']
})
export class EbMaterialGoodsModalComponent implements OnInit {
    data: any[];
    newList: IMaterialGoods[];
    materialGoods: IMaterialGoods[];
    mgCodeFilter: string;
    mgNameFilter: string;
    materialGoodsFilter: IMaterialGoods[];
    account: any;
    constructor(
        private eventManager: JhiEventManager,
        private loginService: LoginService,
        private stateStorageService: StateStorageService,
        private elementRef: ElementRef,
        private renderer: Renderer,
        private router: Router,
        private activeModal: NgbActiveModal,
        public utilsService: UtilsService,
        private materialGoodsService: MaterialGoodsService,
        public translate: TranslateService,
        private principal: Principal
    ) {
        this.materialGoods = [];
    }

    ngOnInit(): void {
        this.principal.identity().then(account => {
            this.account = account;
            this.newList = [];
            if (this.data) {
                this.newList.push(...this.data);
            }
            this.getMaterialGoods();
        });
    }

    getMaterialGoods() {
        this.materialGoodsService
            .getAllMaterialGoodsDTO({ companyID: this.account.organizationUnit.id })
            .subscribe((res: HttpResponse<IMaterialGoods[]>) => {
                this.materialGoods = res.body
                    .filter(x => x.materialGoodsType !== MATERIAL_GOODS_TYPE.DIFF && x.materialGoodsType !== MATERIAL_GOODS_TYPE.SERVICE)
                    .sort((a, b) => a.materialGoodsCode.localeCompare(b.materialGoodsCode));
                if (this.newList) {
                    this.materialGoods.forEach(item => {
                        item.checked = this.newList.some(data => data.id === item.id);
                    });
                }
                this.materialGoodsFilter = this.materialGoods;
            });
    }

    changeMGFilter() {
        if (this.mgNameFilter && this.mgCodeFilter) {
            this.materialGoodsFilter = this.materialGoods.filter(
                x =>
                    x.materialGoodsCode.toLowerCase().includes(this.mgCodeFilter.toLowerCase()) &&
                    x.materialGoodsName.toLowerCase().includes(this.mgNameFilter.toLowerCase())
            );
        } else if (this.mgCodeFilter) {
            this.materialGoodsFilter = this.materialGoods.filter(x =>
                x.materialGoodsCode.toLowerCase().includes(this.mgCodeFilter.toLowerCase())
            );
        } else if (this.mgNameFilter) {
            this.materialGoodsFilter = this.materialGoods.filter(x =>
                x.materialGoodsName.toLowerCase().includes(this.mgNameFilter.toLowerCase())
            );
        } else {
            this.materialGoodsFilter = this.materialGoods;
        }
    }

    apply() {
        this.eventManager.broadcast({
            name: 'selectMaterialGoods',
            content: this.newList
        });
        this.activeModal.dismiss(true);
    }

    close() {
        this.activeModal.dismiss(false);
        this.eventManager.broadcast({
            name: 'selectMaterialGoods'
        });
    }

    isCheckAll() {
        return this.materialGoods.every(item => item.checked) && this.materialGoods.length;
    }

    checkAll() {
        const isCheck = this.materialGoods.every(item => item.checked) && this.materialGoods.length;
        this.materialGoods.forEach(item => (item.checked = !isCheck));

        if (!isCheck) {
            for (let j = 0; j < this.materialGoods.length; j++) {
                let isPush = true;
                for (let i = 0; i < this.newList.length; i++) {
                    if (this.newList[i].id === this.materialGoods[j].id) {
                        isPush = true;
                    }
                }
                if (isPush) {
                    this.newList.push(this.materialGoods[j]);
                }
            }
        } else {
            for (let j = 0; j < this.materialGoods.length; j++) {
                for (let i = 0; i < this.newList.length; i++) {
                    if (this.newList[i].id === this.materialGoods[j].id) {
                        this.newList.splice(i, 1);
                        i--;
                    }
                }
            }
        }
    }

    check(materialGood: IMaterialGoods) {
        materialGood.checked = !materialGood.checked;
        if (materialGood.checked) {
            this.newList.push(materialGood);
        } else {
            for (let i = 0; i < this.newList.length; i++) {
                if (this.newList[i].id === materialGood.id) {
                    this.newList.splice(i, 1);
                    i--;
                }
            }
        }
    }
}
