import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { JhiLanguageHelper, Principal, User, UserService } from 'app/core';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { selector } from 'rxjs-compat/operator/publish';
import { ICostSet } from 'app/shared/model/cost-set.model';
import { IEbGroup } from 'app/core/eb-group/eb-group.model';
import { TreeviewItem } from 'app/shared/tree-combo-box/tree-item';
import { OrganizationUnitService } from 'app/danhmuc/organization-unit';
import { JhiEventManager } from 'ng-jhipster';
import { EbGroupService } from 'app/phan-quyen/eb-group';
import { OrganizationUnitTree } from 'app/shared/model/organization-unit-tree/organization-unit-tree.model';
import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';

@Component({
    selector: 'eb-organization-unit-modal',
    templateUrl: './eb-organization-unit-modal.component.html',
    styleUrls: ['./eb-organization-unit-modal.component.css']
})
export class EbOrganizationUnitModalComponent implements OnInit {
    @Input() public orgs: any[];
    languages: any[];
    authorities: any[];
    isSaving: boolean;
    user: any;
    data: any;
    orgUnits: OrganizationUnitTree[];

    constructor(
        private languageHelper: JhiLanguageHelper,
        private ebGroupService: EbGroupService,
        private route: ActivatedRoute,
        private router: Router,
        private principal: Principal,
        public activeModal: NgbActiveModal,
        private toastr: ToastrService,
        private translate: TranslateService,
        public utilsService: UtilsService,
        private orgService: OrganizationUnitService,
        private eventManager: JhiEventManager
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.orgs = this.orgs ? this.orgs : [];
        this.orgService.getAllOuTreeByOrgId().subscribe(res => {
            this.orgUnits = res.body;
            console.log('orgs OnInit:');
            console.log(this.orgs);
            if (this.orgs.length > 0) {
                this.setCheckbox();
            } else {
                this.setValueDefault();
            }
        });
    }

    setCheckbox() {
        this.orgUnits.forEach(item => {
            const checked = this.orgs.some(org => org.value === item.value);
            item.check = checked ? checked : item.check;
            item.workingOnBook = checked ? this.orgs.find(x => x.value === item.value).workingOnBook : 2;
            // item.collapse = item.children.some(x => x.check);
            // check children
            item.children.forEach(item2 => {
                const checked2 = this.orgs.some(org => org.value === item2.value);
                item2.check = checked2 ? checked2 : item2.check;
                item2.workingOnBook = checked2 ? this.orgs.find(x => x.value === item2.value).workingOnBook : 2;
            });
        });
    }

    setValueDefault() {
        this.orgUnits.forEach(item => {
            item.check = false;
            item.workingOnBook = 2;
            // check children
            item.children.forEach(item2 => {
                item2.check = false;
                item2.workingOnBook = 2;
            });
        });
    }

    previousState() {
        window.history.back();
    }

    apply() {
        const result = [];
        this.orgUnits.forEach(item => {
            if (item.check) {
                result.push(item);
            }
            // check children
            item.children.forEach(item2 => {
                if (item2.check) {
                    result.push(item2);
                }
            });
        });
        console.log('APPLY:');
        console.log(result);
        this.eventManager.broadcast({
            name: 'selectOrgUnits',
            content: result
        });
        this.activeModal.dismiss(true);
    }

    private onSaveSuccess(result) {
        this.isSaving = false;
        this.close();
        this.previousState();
        // this.router.navigate(['admin/eb-group']);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    isCheckAll() {
        const isCheckedAllParents = this.orgUnits ? this.orgUnits.every(item => item.check) : false;
        let isCheckedAllChildren = false;
        if (this.orgUnits ? this.orgUnits.some(item => item.children.length > 0) : false) {
            for (let i = 0; i < this.orgUnits.length; i++) {
                if (this.orgUnits[i].children.length > 0) {
                    const isCheck = this.orgUnits[i].children.every(x => x.check);
                    if (!isCheck) {
                        isCheckedAllChildren = false;
                        break;
                    } else {
                        isCheckedAllChildren = true;
                    }
                }
            }
        } else {
            isCheckedAllChildren = true;
        }
        return isCheckedAllChildren && isCheckedAllParents;
    }

    checkAll() {
        const isCheck = this.isCheckAll();
        this.orgUnits.forEach(item => {
            item.check = !isCheck;
            item.children.forEach(child => {
                child.check = !isCheck;
            });
        });
    }

    check(org: any) {
        org.check = !org.check;
    }

    newArr(length: number): any[] {
        if (length > 0) {
            return new Array(length);
        } else {
            return new Array(0);
        }
    }

    close() {
        this.activeModal.dismiss(false);
    }

    getUnitType(unitType: number): string {
        if (unitType === 0) {
            return 'Tổng công ty/Công ty';
        } else if (unitType === 1) {
            return 'Chi nhánh';
        }
    }

    toggleChildren(org: any, index: number) {
        org.collapse = !org.collapse;
    }
}
