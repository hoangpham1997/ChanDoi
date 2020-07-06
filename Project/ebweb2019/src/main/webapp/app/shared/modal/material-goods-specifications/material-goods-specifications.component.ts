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
import { DiscountAllocationModalComponent } from 'app/shared/modal/discount-allocation/discount-allocation-modal.component';
import { MaterialGoodsSpecifications } from 'app/shared/model/material-goods-specifications.model';
import { MaterialGoodsSpecificationsService } from 'app/entities/material-goods-specifications';
import { ToastrService } from 'ngx-toastr';

@Component({
    selector: 'eb-material-goods-cpecifications-modal',
    templateUrl: './material-goods-specifications.component.html',
    styleUrls: ['./material-goods-specifications.component.css']
})
export class EbMaterialGoodsSpecificationsModalComponent implements OnInit {
    data: any;
    typeID: any;
    account: any;
    materialGoodsSpecification: any[];
    materialGoodsSpecificationLedger: any[];
    header: any[];
    materialGoodsID: string;
    repositoryID: string;
    materialGoodsCode: string;
    materialGoodsName: string;
    FORM_NHAP = 1;
    FORM_XUAT = 2;
    newList: any[];
    modalData: any[];
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
        private principal: Principal,
        private toasService: ToastrService,
        private materialGoodsSpecificationsService: MaterialGoodsSpecificationsService
    ) {}

    ngOnInit(): void {
        this.newList = [];
        this.repositoryID = '';
        this.materialGoodsCode = '';
        this.materialGoodsName = '';
        this.header = [{ name: '' }, { name: '' }, { name: '' }, { name: '' }, { name: '' }];
        this.materialGoodsID = null;
        if (this.data.materialGoodsId) {
            this.materialGoodsID = this.data.materialGoodsId;
        } else if (this.data.materialGoodsID) {
            this.materialGoodsID = this.data.materialGoodsID;
        } else if (this.data.materialGoods) {
            this.materialGoodsID = this.data.materialGoods.id;
        }
        if (this.data.materialGoodsCode) {
            this.materialGoodsCode = this.data.materialGoodsCode;
        } else if (this.data.materialGoods) {
            this.materialGoodsCode = this.data.materialGoods.materialGoodsCode;
        } else if (this.data.materialGood) {
            this.materialGoodsCode = this.data.materialGood.materialGoodsCode;
        }
        if (this.data.materialGoodsName) {
            this.materialGoodsName = this.data.materialGoodsName;
        } else if (this.data.materialGoods) {
            this.materialGoodsName = this.data.materialGoods.materialGoodsName;
        } else if (this.data.materialGood) {
            this.materialGoodsName = this.data.materialGood.materialGoodsName;
        }
        if (this.data.repositoryID) {
            this.repositoryID = this.data.repositoryID;
        } else if (this.data.repository) {
            this.repositoryID = this.data.repository.id;
        }

        this.materialGoodsSpecificationLedger = [];
        this.newList.push(...this.modalData);
        if (this.modalData && this.typeID === this.FORM_NHAP) {
            this.materialGoodsSpecificationLedger.push(...this.modalData);
        }
        this.materialGoodsSpecification = [];
        this.principal.identity().then(account => {
            this.account = account;
            this.getMaterialGoodsSpecification(this.materialGoodsID);
            if (this.typeID === this.FORM_XUAT) {
                this.getMaterialGoodsSpecificationLedger(this.materialGoodsID, this.repositoryID);
            }
        });
    }

    getMaterialGoodsSpecification(id) {
        this.materialGoodsSpecificationsService.findByMaterialGoodsID({ id }).subscribe((res: HttpResponse<any[]>) => {
            this.materialGoodsSpecification = res.body;
            for (let i = 0; i < this.materialGoodsSpecification.length; i++) {
                this.header[i].name = this.materialGoodsSpecification[i].materialGoodsSpecificationsName;
            }
        });
    }

    getMaterialGoodsSpecificationLedger(id, repositoryID) {
        this.materialGoodsSpecificationsService.findLedgerByMaterialGoodsID({ id, repositoryID }).subscribe((res: HttpResponse<any[]>) => {
            this.materialGoodsSpecificationLedger = res.body;
            if (this.newList) {
                this.newList.forEach(item => {
                    this.materialGoodsSpecificationLedger[item.rowIndex - 1].checked = true;
                });
            }
        });
    }

    apply() {
        if (this.typeID === this.FORM_NHAP) {
            if (!this.data.quantity || this.data.quantity === this.sum('iWQuantity')) {
                this.eventManager.broadcast({
                    name: 'materialGoodsSpecifications',
                    content: this.materialGoodsSpecificationLedger
                });
                this.activeModal.dismiss(true);
            } else {
                this.toasService.error(this.translate.instant('ebwebApp.materialGoodsSpecifications.notEqualsQuantity'));
            }
        } else {
            const totalOWQuantity = this.newList.reduce(function(prev, cur) {
                return prev + cur.oWQuantity;
            }, 0);
            if (!this.data.quantity || this.data.quantity === totalOWQuantity) {
                this.eventManager.broadcast({
                    name: 'materialGoodsSpecifications',
                    content: this.newList
                });
                this.activeModal.dismiss(true);
            } else {
                this.toasService.error(this.translate.instant('ebwebApp.materialGoodsSpecifications.notEqualsQuantity'));
            }
        }
    }

    close() {
        this.activeModal.dismiss(false);
    }

    newArr(lenght: number): any[] {
        if (lenght > 0) {
            return new Array(lenght);
        } else {
            return new Array(0);
        }
    }

    addNewRow() {
        this.materialGoodsSpecificationLedger.push({
            orderPriority: this.materialGoodsSpecificationLedger.length + 1,
            materialGoodsID: this.materialGoodsID,
            quantity: 0
        });
    }

    deleteRow(value: number) {
        this.materialGoodsSpecificationLedger.splice(value, 1);
    }

    sum(prop) {
        let total = 0;
        for (let i = 0; i < this.materialGoodsSpecificationLedger.length; i++) {
            total += this.materialGoodsSpecificationLedger[i][prop];
        }
        return isNaN(total) ? 0 : total;
    }

    isCheckAll() {
        return this.materialGoodsSpecificationLedger.every(item => item.checked) && this.materialGoodsSpecificationLedger.length;
    }

    checkAll() {
        const isCheck = this.materialGoodsSpecificationLedger.every(item => item.checked) && this.materialGoodsSpecificationLedger.length;
        this.materialGoodsSpecificationLedger.forEach(item => (item.checked = !isCheck));

        if (!isCheck) {
            for (let j = 0; j < this.materialGoodsSpecificationLedger.length; j++) {
                this.newList.push(this.materialGoodsSpecificationLedger[j]);
                this.materialGoodsSpecificationLedger[j].oWQuantity = this.materialGoodsSpecificationLedger[j].iWQuantity;
            }
        } else {
            this.newList = [];
        }
    }

    check(materialGoodsSpecificationLedger: any) {
        materialGoodsSpecificationLedger.checked = !materialGoodsSpecificationLedger.checked;
        if (materialGoodsSpecificationLedger.checked) {
            this.newList.push(materialGoodsSpecificationLedger);
            materialGoodsSpecificationLedger.oWQuantity = materialGoodsSpecificationLedger.iWQuantity;
        } else {
            const index = this.newList.indexOf(materialGoodsSpecificationLedger);
            this.newList.splice(index, 1);
        }
    }

    changeOWQuantity(item) {
        if (item.oWQuantity > item.iWQuantity) {
            this.toasService.error(this.translate.instant('ebwebApp.materialGoodsSpecifications.moreThanIWQuantity'));
        }
    }
}
