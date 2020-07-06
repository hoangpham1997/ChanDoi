import { Component, forwardRef, Input, OnInit, Output } from '@angular/core';
import { TreeviewItem } from 'app/shared/tree-combo-box/tree-item';
import { EventEmitter } from '@angular/core';
import { CheckService } from 'app/shared/tree-combo-box/check-service';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import * as moment from 'moment';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { TreeAccountListItem } from 'app/shared/tree-grid/tree-item';

const noop = () => {};

export const CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR: any = {
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => TreeViewComponent),
    multi: true
};

@Component({
    selector: 'eb-tree-combo-box-item',
    templateUrl: './tree-combo-box-item.component.html',
    styleUrls: ['./tree-combo-box.css']
})
export class TreeViewItemComponent implements OnInit {
    @Input() tree: TreeAccountListItem[];
    @Input() options: any;
    @Output() onCheckItem = new EventEmitter();
    isRequired: boolean;

    constructor(private checkService: CheckService) {}

    ngOnInit() {
        this.isRequired = true;
    }

    selectData(treeItem) {
        this.checkService.checked(treeItem);
    }
}

@Component({
    selector: 'eb-tree-combo-box',
    templateUrl: './tree-view.component.html',
    styleUrls: ['./tree-combo-box.css'],
    providers: [CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR]
})
export class TreeViewComponent implements OnInit, ControlValueAccessor {
    @Input() tree: TreeAccountListItem[];
    data: TreeAccountListItem;
    showDropDown: boolean;
    isOverBottomPage: boolean;
    overFlow: boolean;
    inSide: boolean;
    private onTouchedCallback: () => void = noop;
    private onChangeCallback: (_: any) => void = noop;
    isRequired: boolean;
    search: string;
    isFound: boolean;

    constructor(private checkService: CheckService, private toastr: ToastrService, private translate: TranslateService) {
        this.showDropDown = false;
        this.checkService.checkEvent.subscribe(() => {
            if (this.checkService.treeItem !== null) {
                this.showDropDown = false;
            }
            this.value = this.checkService.treeItem;
        });
    }

    ngOnInit() {
        this.isRequired = true;
    }

    // get accessor
    get value(): any {
        return this.data;
    }

    // set accessor including call the onchange callback
    set value(v: any) {
        if (v !== this.data) {
            this.data = v;
            if (this.data !== null) {
                this.search = this.data.parent.organizationUnitName;
            }
            this.onChangeCallback(v);
        }
    }

    toggleDropDown(event, showAll = false): void {
        this.showDropDown = !this.showDropDown;
        // prevent overflow page
        if (event.clientX > 1200) {
            this.overFlow = true;
        }
        if (event.clientY > 260) {
            this.isOverBottomPage = true;
        }
        if (showAll) {
            this.searchTree(null, showAll);
        }
    }

    selectData(treeItem) {
        if (treeItem !== null) {
            this.showDropDown = false;
            this.search = treeItem.parent.organizationUnitName;
        }
        this.checkService.checked(treeItem);
    }

    mouseOverTable() {
        this.inSide = true;
    }

    mouseLeaveTable() {
        this.inSide = false;
    }

    registerOnChange(fn: any): void {
        this.onChangeCallback = fn;
    }

    registerOnTouched(fn: any): void {
        this.onTouchedCallback = fn;
    }

    setDisabledState(isDisabled: boolean): void {}

    writeValue(value: any): void {
        this.data = value;
        if (this.data && this.data.parent) {
            this.search = this.data.parent.organizationUnitName;
        }
    }

    searchTree(tree = null, showAll = false) {
        if (tree === null) {
            tree = this.tree;
        }
        if (!showAll) {
            this.showDropDown = true;
        }
        if (this.search && !showAll) {
            tree.forEach(item => {
                if (!item.parent.organizationUnitName.toLowerCase().includes(this.search.toLowerCase())) {
                    item.isHidden = true;
                } else {
                    item.isHidden = false;
                }
                if (item.children) {
                    this.searchTree(item.children, showAll);
                }
                if (item.parent.organizationUnitName.toLowerCase() === this.search.toLowerCase()) {
                    this.selectData(item);
                    this.isFound = true;
                } else {
                    this.selectData(null);
                    this.isFound = false;
                }
            });
        } else {
            if (!this.search) {
                this.selectData(null);
            }
            tree.forEach(item => {
                item.isHidden = false;
                if (item.children) {
                    this.searchTree(item.children, showAll);
                }
            });
        }
    }

    outFocus() {
        setTimeout(() => {
            if (this.search && !this.isFound && !this.data) {
                this.toastr.warning(
                    this.translate.instant('global.combobox.dataNotExist'),
                    this.translate.instant('global.combobox.error')
                );
            }
            if (!this.inSide) {
                this.showDropDown = false;
            }
        }, 100);
    }
}
