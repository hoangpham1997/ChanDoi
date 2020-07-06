import { Component, EventEmitter, forwardRef, HostListener, Input, OnInit, Output, ViewChild } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { TreeAccountListItem } from 'app/shared/tree-grid/tree-item';
import { TranslateService } from '@ngx-translate/core';
import { Router } from '@angular/router';
import { JhiEventManager } from 'ng-jhipster';
import { ToastrService } from 'ngx-toastr';
import { CheckTreeGridService } from 'app/shared/tree-grid/check-tree-grid-service';
import { Tree } from '@angular/router/src/utils/tree';
import { ITreeAccountList } from 'app/shared/model/account-list-tree.model';
import { start } from 'repl';
import { current } from 'codelyzer/util/syntaxKind';

const noop = () => {};

export const CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR: any = {
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => TreeGridComponent),
    multi: true
};

@Component({
    selector: 'eb-tree-grid-item',
    templateUrl: './tree-grid-item.component.html',
    styleUrls: ['./tree-grid.css']
})
export class TreeGridItemComponent implements OnInit {
    @Input() tree: TreeAccountListItem[];
    @Input() options: any;
    @Input() data: any[];
    @Input() listKey: any[];
    @Input() generalWidth: string;
    @Input() isOrganizationUnit?: boolean;
    @Input() nameWidth: string;
    @Input() checkWidth: string;
    @Output() onCheckItem = new EventEmitter();
    @Input() navigateForm: string;
    @Input() account: any;
    @Input() isHome: boolean;
    @Input() isPopup: boolean;
    isRequired: boolean;
    selectedRow: TreeAccountListItem;
    objects: any[];
    selectedRows = [];
    @Input() nameSearch: string;

    constructor(
        private checkService: CheckTreeGridService,
        private translate: TranslateService,
        private router: Router,
        private eventManager: JhiEventManager,
        private toastr: ToastrService
    ) {}

    ngOnInit() {
        if (!this.nameSearch) {
            this.nameSearch = '';
        }
        this.checkService.selectedRows = [];
        this.isRequired = true;
        this.objects = this.tree;
    }

    changeSelect(i) {
        this.tree[i].select = !this.tree[i].select;
    }

    onSelect(select: TreeAccountListItem, evt?) {
        if (evt) {
            evt.preventDefault();
            if (evt.shiftKey) {
                this.checkService.checked(null);
                this.checkService.isMultiRow = true;
                this.checkService.multiSelect(this.checkService.startItem, select);
                this.eventManager.broadcast({
                    name: 'selectRows',
                    content: 'Select rows',
                    data: this.checkService.selectedRows
                });
                this.selectedRow = select;
                this.checkService.startItem = select;
            } else if (evt.ctrlKey || evt.metaKey) {
                this.selectedRows.push(select);
                select.check = !select.check;
                this.checkService.checked(select);
                this.checkService.isMultiRow = true;
                this.eventManager.broadcast({
                    name: 'selectRows',
                    content: 'Select rows',
                    data: this.checkService.selectedRows
                });
                this.eventManager.broadcast({
                    name: 'selectRow',
                    content: 'Select row',
                    data: null
                });
                this.checkService.startItem = select;
                this.selectedRow = select;
            } else {
                this.selectedRows = [];
                this.checkService.selectedRows = [];
                this.selectedRow = select;
                this.checkService.startItem = select;
                this.selectedRows.push(select);
                this.checkService.isMultiRow = false;
                this.checkService.checked(select);
                select.check = true;
                this.eventManager.broadcast({
                    name: 'selectRow',
                    content: 'Select row',
                    data: select
                });
            }
        } else {
            this.selectedRows = [];
            this.checkService.selectedRows = [];
            this.selectedRow = select;
            this.checkService.startItem = select;
            this.selectedRows.push(select);
            this.checkService.isMultiRow = false;
            this.checkService.checked(select);
            select.check = true;
            this.eventManager.broadcast({
                name: 'selectRow',
                content: 'Select row',
                data: select
            });
        }
    }

    selectChangeCheck() {
        this.checkService.checkEvent.emit();
    }

    setWidth(head) {
        if (head === this.nameSearch || head === 'checked') {
            return this.checkWidth;
        } else if (head.includes('Name')) {
            return this.nameWidth;
        } else {
            return this.generalWidth;
        }
    }

    @HostListener('document:keydown.enter')
    edit() {
        if (this.selectedRow && this.selectedRow.parent && this.selectedRow.parent.id && this.selectedRows.length === 1) {
            sessionStorage.setItem('currentRow', JSON.stringify(this.selectedRow));
            this.router.navigate([this.navigateForm, this.selectedRow.parent.id, 'edit']);
        }
    }
}

@Component({
    selector: 'eb-tree-grid',
    templateUrl: './tree-grid-view.component.html',
    styleUrls: ['./tree-grid.css'],
    providers: [CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR]
})
export class TreeGridComponent implements OnInit, ControlValueAccessor {
    _tree: TreeAccountListItem[];
    @Input() dataList: any[];
    @Input() listTHead: string[];
    @Input() listKey: any[];
    @Input() navigateForm: string;
    @Input() height: number;
    @Input() isHome: boolean;
    @Input() account: any;
    @Input() isShowSearch?: boolean;
    @Input() isOrganizationUnit?: boolean;
    @Input() listSearch?: any;
    @Input() keySearchs?: any[];
    @Input() isPopup?: boolean;
    data: TreeAccountListItem;
    showDropDown: boolean;
    private onTouchedCallback: () => void = noop;
    private onChangeCallback: (_: any) => void = noop;
    isRequired: boolean;
    lengthTHead: number;
    generalWidth: string;
    nameWidth: string;
    checkWidth: string;
    selectedRow: TreeAccountListItem;
    startTree: TreeAccountListItem;
    endTree: TreeAccountListItem;
    objects: any[];
    selectedRows = [];
    currentRow: TreeAccountListItem;
    currentData: TreeAccountListItem;
    nextIsParent: boolean;
    count: number;
    treeCount: number;
    status: boolean;
    indexRow: number;
    isAdd: boolean;
    isCheckAll: boolean;
    @Input() nameSearch: string;
    @ViewChild('child') child: TreeGridItemComponent;

    // currentObject: TreeAccountListItem;

    constructor(private checkService: CheckTreeGridService, private eventManager: JhiEventManager) {
        this.showDropDown = false;
        this.objects = this._tree;
        this.checkService.checkEvent.subscribe(() => {
            if (this.isPopup) {
                this.eventManager.broadcast({
                    name: 'sendTree' + (this.nameSearch ? this.nameSearch : ''),
                    content: 'sendTree' + (this.nameSearch ? this.nameSearch : ''),
                    data: this._tree
                });
            }
            if (checkService.isMultiRow) {
                if (this.checkService.treeItemGrid) {
                    // if (!this.checkService.selectedRows.filter(a => a === this.checkService.treeItemGrid)) {
                    this.checkService.selectedRows.push(this.checkService.treeItemGrid);
                    // } else {
                    //     this.checkService.selectedRows.splice(this.checkService.selectedRows.indexOf(this.checkService.treeItemGrid), 1);
                    // }
                } else {
                    this.treeCount = 0;
                    this.isAdd = false;
                    this.status = false;
                    this.startTree = checkService.startTreeItemGrid;
                    this.endTree = checkService.endTreeItemGrid;
                    this.findIndexRow(this._tree, 0, this.startTree);
                    const startIndex = this.indexRow;
                    this.treeCount = 0;
                    this.findIndexRow(this._tree, 0, this.endTree);
                    const endIndex = this.indexRow;
                    if (startIndex > endIndex) {
                        let temp;
                        temp = this.endTree;
                        this.endTree = this.startTree;
                        this.startTree = temp;
                    }
                    this.shiftSelectRows(this.startTree, this.endTree, this._tree, 0);
                }
            } else {
                this.currentRow = checkService.treeItemGrid;
                this.unCheckTree(this._tree, 0);
            }
        });
    }

    @Input()
    set tree(val: any) {
        this._tree = val;
        this.status = false;
        this.isAdd = false;
        this.count = 0;
        this.treeCount = 0;
        if (this._tree && this._tree[0]) {
            this.countTree(this._tree, 0);
            // lưu lại dòng đang chọn khi sửa
            if (sessionStorage.getItem('currentRow')) {
                this.currentRow = JSON.parse(sessionStorage.getItem('currentRow'));
                sessionStorage.removeItem('currentRow');
                this.findCurrentRow(this._tree, 0);
                this.eventManager.broadcast({
                    name: 'selectRow',
                    content: 'Select row',
                    data: this.currentData
                });
            } else {
                this._tree[0].check = true;
                this.eventManager.broadcast({
                    name: 'selectRow',
                    content: 'Select row',
                    data: this._tree[0]
                });
            }
        }
    }

    // shift chọn nhiều dòng
    shiftSelectRows(startTree: TreeAccountListItem, endTree: TreeAccountListItem, tree: TreeAccountListItem[], grade) {
        if (tree) {
            if (startTree && startTree.parent && startTree.parent.id) {
                for (let i = 0; i < tree.length; i++) {
                    if (tree[i].parent.id === startTree.parent.id) {
                        this.isAdd = true;
                        this.status = true;
                        this.checkService.selectedRows.push(tree[i]);
                        tree[i].check = this.status;
                    } else if (tree[i].parent.id === endTree.parent.id) {
                        tree[i].check = this.status;
                        this.isAdd = false;
                        this.checkService.selectedRows.push(tree[i]);
                        this.status = false;
                    } else {
                        if (this.isAdd) {
                            this.checkService.selectedRows.push(tree[i]);
                        }
                        tree[i].check = this.status;
                    }
                    if (tree[i].children && tree[i].children.length > 0) {
                        this.shiftSelectRows(startTree, endTree, tree[i].children, grade + 1);
                    }
                }
            }
        }
    }

    // tìm lại dòng hiện tại khi chuyển trang rồi quay lại
    findCurrentRow(tree: TreeAccountListItem[], grade) {
        if (tree) {
            for (let i = 0; i < tree.length; i++) {
                if (tree[i].parent.id === this.currentRow.parent.id) {
                    tree[i].check = true;
                    this.currentData = tree[i];
                }
                if (tree[i].children && tree[i].children.length > 0) {
                    this.findCurrentRow(tree[i].children, grade + 1);
                }
            }
        }
    }

    // bỏ check tất cả trừ cái đang chọn
    unCheckTree(tree: TreeAccountListItem[], grade) {
        if (tree) {
            for (let i = 0; i < tree.length; i++) {
                if (tree[i].parent !== this.checkService.treeItemGrid && tree[i].parent.id !== this.checkService.parentID) {
                    tree[i].check = false;
                }
                if (tree[i].children && tree[i].children.length > 0) {
                    this.unCheckTree(tree[i].children, grade + 1);
                }
            }
        }
    }

    // Đếm số dòng của cây
    countTree(tree: TreeAccountListItem[], grade) {
        if (tree) {
            for (let i = 0; i < tree.length; i++) {
                this.count++;
                if (tree[i].children && tree[i].children.length > 0) {
                    this.countTree(tree[i].children, grade + 1);
                }
            }
        }
    }

    // Tìm vị trí hiện tại của giá trị
    findIndexRow(tree: TreeAccountListItem[], grade, currentTree) {
        if (tree) {
            for (let i = 0; i < tree.length; i++) {
                this.treeCount++;
                if (currentTree && currentTree.parent && tree[i].parent.id === currentTree.parent.id) {
                    this.indexRow = this.treeCount;
                }
                if (tree[i].children && tree[i].children.length > 0) {
                    this.findIndexRow(tree[i].children, grade + 1, currentTree);
                }
            }
        }
    }

    // Ctrl A chọn tất cả dữ liệu
    @HostListener('document:keydown.control.a')
    selectAll() {
        event.preventDefault();
        if (!this.isPopup) {
            this.checkAll(this._tree, 0);
        } else {
            this.checkAllPopUp(this._tree, 0);
            this.eventManager.broadcast({
                name: 'sendTree' + (this.nameSearch ? this.nameSearch : ''),
                content: 'sendTree' + (this.nameSearch ? this.nameSearch : ''),
                data: this._tree
            });
        }
    }

    checkAllPopUp(tree: TreeAccountListItem[], grade) {
        if (tree) {
            for (let i = 0; i < tree.length; i++) {
                tree[i].parent.checked = this.isCheckAll;
                if (tree[i].children && tree[i].children.length > 0) {
                    this.checkAllPopUp(tree[i].children, grade + 1);
                }
            }
        }
    }

    checkAll(tree: TreeAccountListItem[], grade) {
        if (tree) {
            for (let i = 0; i < tree.length; i++) {
                tree[i].check = true;
                if (tree[i].children && tree[i].children.length > 0) {
                    this.checkAll(tree[i].children, grade + 1);
                }
            }
        }
    }

    ngOnInit() {
        if (this.listTHead) {
            this.lengthTHead = this.listTHead.length;
        }
        console.log(this.keySearchs);
        console.log(this.listSearch);
        let gWidth;
        let checkWidth = 0;
        if (this.isOrganizationUnit || this.isHome) {
            gWidth = 100 / (this.lengthTHead + 1);
        } else if (this.isPopup) {
            gWidth = 90 / this.lengthTHead;
            checkWidth = 10;
        } else {
            gWidth = 100 / this.lengthTHead;
        }
        this.generalWidth = gWidth + '%';
        this.checkWidth = checkWidth + '%';
        this.nameWidth = gWidth * 2 + '%';
        this.isRequired = true;
        this.nextIsParent = false;
    }

    // get accessor
    get value(): any {
        return this.tree;
    }

    // set accessor including call the onchange callback
    set value(v: any) {
        if (v !== this.tree) {
            this.tree = v;
            this.onChangeCallback(v);
        }
    }

    @HostListener('document:keydown.ArrowUp', ['$event'])
    @HostListener('document:keydown.ArrowDown', ['$event'])
    /***
     *
     * @param $event
     * @param scroll 'UP' or 'DOWN' only
     */
    scrollItemSelected($event: KeyboardEvent, scroll?: string) {
        // class table
        const parentContainer = document.getElementsByClassName('voucher-table')[0];
        // class item selected
        const element = document.getElementsByClassName('selected')[0];
        // class header
        const headerTable = document.getElementsByClassName('header-table')[0];
        if (parentContainer === undefined || element === undefined || headerTable === undefined) {
            return false;
        }
        const elRect = element.getBoundingClientRect(),
            parRect = parentContainer.getBoundingClientRect(),
            headerRect = headerTable.getBoundingClientRect();
        const elementHeight = elRect.height;
        if (scroll && scroll === 'UP') {
            if (elRect.top <= parRect.top + elementHeight) {
                parentContainer.scrollTop = parentContainer.scrollTop - (parRect.top + elementHeight - elRect.top) - headerRect.height;
            }
        }
        if (scroll && scroll === 'DOWN') {
            if (!(elRect.top >= parRect.top && elRect.bottom <= parRect.bottom && elRect.bottom + elementHeight <= parRect.bottom)) {
                parentContainer.scrollTop = parentContainer.scrollTop + elRect.height + (elRect.bottom - parRect.bottom);
            }
        }
        if ($event && $event.key === 'ArrowDown') {
            // down arrow
            if (!(elRect.top >= parRect.top && elRect.bottom <= parRect.bottom && elRect.bottom + elementHeight <= parRect.bottom)) {
                parentContainer.scrollTop = parentContainer.scrollTop + elRect.height + (elRect.bottom - parRect.bottom);
            }
        } else {
            // up arrow
            if (elRect.top <= parRect.top + elementHeight) {
                parentContainer.scrollTop = parentContainer.scrollTop - (parRect.top + elementHeight - elRect.top) - headerRect.height;
            }
        }
    }

    registerOnChange(fn: any): void {
        this.onChangeCallback = fn;
    }

    registerOnTouched(fn: any): void {
        this.onTouchedCallback = fn;
    }

    setDisabledState(isDisabled: boolean): void {}

    writeValue(value: any): void {
        this.tree = value;
    }

    onSelect(select: any, evt?) {
        this.child.onSelect(select, evt);
    }

    newArr(lenght: number): any[] {
        if (lenght > 0) {
            return new Array(lenght);
        } else {
            return new Array(0);
        }
    }

    changeListSearch() {
        this.eventManager.broadcast({
            name: 'listSearch' + (this.nameSearch ? this.nameSearch : ''),
            content: 'listSearch' + (this.nameSearch ? this.nameSearch : ''),
            data: this.listSearch
        });
    }

    setWidth(head) {
        if (!this.isPopup && (head.includes('organizationUnitName') || (this.isHome && head.includes('accountName')))) {
            return this.nameWidth;
        } else if (this.isPopup) {
            if (head === 'checked' || head === this.nameSearch) {
                return this.checkWidth;
            } else if (head.includes('Name')) {
                return this.nameWidth;
            } else {
                return this.generalWidth;
            }
        } else {
            return this.generalWidth;
        }
    }

    checkClassScroll() {
        if (!this.isPopup) {
            if (this.count > 21) {
                return 'margin-scroll';
            } else {
                return 'margin-not-scroll';
            }
        } else {
            if (this.count > 13) {
                return 'margin-scroll';
            } else {
                return 'margin-not-scroll';
            }
        }
    }
}
