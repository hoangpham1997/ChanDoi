import { Component, ElementRef, EventEmitter, forwardRef, Input, OnChanges, OnInit, Output, SimpleChanges, ViewChild } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { ComboBoxPipe } from './combo-box.pipe';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { isNumber } from 'util';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ComboboxModalService } from 'app/core/login/combobox-modal.service';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';

const noop = () => {};

export const CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR: any = {
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => ComboBoxComponent),
    multi: true
};

export enum KEY_CODE {
    UP_ARROW = 38,
    DOWN_ARROW = 40,
    TAB_KEY = 9
}

// @ts-ignore: Unreachable code error
@Component({
    selector: 'combo-box',
    templateUrl: './combo-box.component.html',
    styleUrls: ['./combo-box.component.css'],
    providers: [ComboBoxPipe, CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR]
})
// author : hautv
// -----
export class ComboBoxComponent implements OnInit, ControlValueAccessor, OnChanges {
    // The internal data model
    private innerValue: any = '';
    @Input() dataList: object[];
    @Input() displayMember: string;
    @Input() idIp: string;
    @Input() nameIp: string;
    @Input() listColumns: string[];
    @Input() headerColumns: string[];
    @Input() isObject: boolean;
    @Input() isSelectDummyList: boolean;
    @Input() valueName: string;
    @Input() isReadOnly: boolean;
    @Input() hiddenHeader: boolean;
    @Input() onSelectedCallback: boolean;
    @Input() isRequired: boolean;
    @Input() hideRequiredWhenValueTrue: boolean;
    // css for z-index
    @Input() isOutTable: boolean;
    @Input() valueIsNumber: boolean;
    @Input() getValueDisplay: boolean;
    @Input() systemOptions: any;
    @Input() account: any;
    @Input() type: any;
    @Input() allowNegative: boolean;
    @Input() nameCategory: string;
    @Input() showIconPlus: boolean;
    @Input() isRemoveValue: boolean;
    @Input() isCheckEmpty: boolean;
    @Input() tabindex: boolean;
    @Input() autoFillFistMatch: boolean;
    @Input() backgroundInherit: boolean;
    @Input() noEdit: boolean;
    @Input() inPutCurrency: string;
    @Input() hasBoder: boolean;
    @Input() isCostSet: boolean;
    @Input() noGetValueParentNode: boolean;
    // @Output() dataIsNotExist = new EventEmitter<boolean>();
    @Input() isRateType: boolean; // Sử dụng cho các ô %
    @Input() showAsInput: boolean; // hiển thị như ô input không phải cbb
    @Input() stypeForHome: boolean;
    @Input() typeObject: number; // phân biệt cbb đối tượng và nhân viên bên TMNH
    @Output() focusInput = new EventEmitter<any>();
    @ViewChild('nameOfInputCombobox') nameField: ElementRef; // sử dụng kick chuột trong vùng div mà không có dữ liệu
    @ViewChild('nameOfInputComboboxNumber') nameField_Number: ElementRef; // sử dụng kick chuột trong vùng div mà không có dữ liệu
    @ViewChild('nameOfInputComboboxNumberIsRateType') nameField_NumberIsRateType: ElementRef; // sử dụng kick chuột trong vùng div mà không có dữ liệu

    idFocus: string;
    dummyDataList: any[];
    showDropDown: boolean;
    counter: number;
    textToSort: string;
    selectRow: any;
    inSide: boolean;
    overFlow: boolean;
    required: boolean;
    bottom100: boolean;
    zindexForDropdown: boolean;
    isOverBottomPage: boolean;
    nonCheckZeroValue: boolean;
    modalRef: NgbModalRef;
    clientX: number;
    clientY: number;
    // Placeholders for the callbacks which are later provided
    // by the Control Value Accessor
    private onTouchedCallback: () => void = noop;
    private onChangeCallback: (_: any) => void = noop;

    // get accessor
    get value(): any {
        return this.innerValue;
    }

    // set accessor including call the onchange callback
    set value(v: any) {
        if (v !== this.innerValue || this.onSelectedCallback) {
            this.innerValue = v;
            this.onChangeCallback(v);
        }
        /*if (!this.getValueDisplay) {
            if ((v === null || v === undefined) && this.textToSort) {
                this.dataIsNotExist.emit(true);
            } else {
                this.dataIsNotExist.emit(false);
            }
        }*/
    }

    // Set touched on blur
    onBlur() {
        this.onTouchedCallback();
    }

    focus() {
        const element = document.activeElement;
        if (element) {
            this.idFocus = element.id;
        }
    }

    // From ControlValueAccessor interface
    writeValue(value: any) {
        /*Comment để sửa lỗi chuyển tag view bị trắng dữ liệu by Hautv*/
        if (this.getValueDisplay) {
            this.textToSort = value;
            // this.value = value;
            if (this.isRequired) {
                if (!this.textToSort) {
                    this.required = true;
                } else {
                    this.required = false;
                }
            }
        } else {
            if (value !== this.innerValue) {
                this.innerValue = value;
                if (this.dataList) {
                    if (this.value || this.value === 0) {
                        if (this.isObject) {
                            this.selectRow = this.value;
                            if (this.displayMember) {
                                this.textToSort = this.value[this.displayMember];
                            }
                        } else {
                            for (const r of this.dataList) {
                                if (r[this.valueName] || r[this.valueName] === 0) {
                                    if (this.valueIsNumber) {
                                        if (r[this.valueName] === this.value) {
                                            this.selectRow = r;
                                            break;
                                        }
                                    } else {
                                        if (String(r[this.valueName]).toUpperCase() === String(this.value).toUpperCase()) {
                                            this.selectRow = r;
                                            break;
                                        }
                                    }
                                }
                            }
                            if (this.selectRow) {
                                if (this.displayMember) {
                                    this.textToSort = this.selectRow[this.displayMember];
                                }
                            } else {
                                this.textToSort = value;
                            }
                        }
                    } else {
                        this.textToSort = null;
                        this.required = false;
                    }
                }
            }
        }
    }

    // From ControlValueAccessor interface
    registerOnChange(fn: any) {
        this.onChangeCallback = fn;
        // console.log(fn);
    }

    // From ControlValueAccessor interface
    registerOnTouched(fn: any) {
        this.onTouchedCallback = fn;
    }

    onFocusEventAction(event): void {
        this.counter = -1;
        if (this.nameField) {
            const nF = this.nameField.nativeElement.getBoundingClientRect();
            this.clientX = nF.x;
            this.clientY = nF.y;
        }
        this.focusInput.emit(event);
    }

    onBlurEventAction(): void {
        // if(this.counter > -1)this.textToSort = this.dataList[this.counter][this.columnName];
        this.showDropDown = false;
    }

    mouseOverTable() {
        this.inSide = true;
    }

    mouseLeaveTable() {
        this.inSide = false;
    }

    // Hautv edit check trường hợp dữ liệu không tồn tại trong danh mục
    outFocus() {
        if (!this.inSide) {
            this.showDropDown = false;
            if (!this.getValueDisplay) {
                if (this.textToSort) {
                    if (!this.value && !this.nonCheckZeroValue && (this.value === undefined || this.value === null)) {
                        if (this.autoFillFistMatch && this.dummyDataList && this.dummyDataList.length > 0) {
                            this.updateTextBox(this.dummyDataList[0]);
                            return;
                        }
                        if (this.dummyDataList) {
                            if (!this.isObject) {
                                if (this.dummyDataList.find(n => n[this.valueName] === this.value)) {
                                    return;
                                }
                            }
                        }

                        this.toastr.warning(
                            this.translate.instant('global.combobox.dataNotExist'),
                            this.translate.instant('global.combobox.error')
                        );
                        // this.nameField.nativeElement.focus();
                        this.required = true;
                        /*if (this.isRemoveValue) {
                            this.textToSort = '';
                            this.required = this.isRequired;
                        }*/
                        // giờ để mặc định xóa luôn text
                        this.textToSort = '';
                        this.required = this.isRequired;
                        // this.dataIsNotExist.emit(true);
                    }
                }
            }
        } else {
            if (this.getValueDisplay && this.valueIsNumber) {
                if (this.isRateType) {
                    this.nameField_NumberIsRateType.nativeElement.focus(); // fix lỗi không đóng combobox
                } else {
                    this.nameField_Number.nativeElement.focus(); // fix lỗi không đóng combobox
                }
            } else {
                this.nameField.nativeElement.focus(); // fix lỗi không đóng combobox
            }
        }
    }

    onKeyDownAction(event: KeyboardEvent): void {
        // console.log('event.keyCode',event.keyCode);
        // Hautv comment
        /*if (this.isSelectDummyList) {
            if (event.keyCode === KEY_CODE.UP_ARROW) {
                this.showDropDown = true;
                this.counter = this.counter === 0 ? this.counter : --this.counter;
                this.checkHighlight(this.counter);
                if (this.displayMember) {
                    if (this.getValueDisplay) {
                        this.textToSort = this.dummyDataList[this.counter][this.displayMember];
                        this.value = this.dummyDataList[this.counter][this.displayMember];
                        if (this.isRequired) {
                            if (!this.value) {
                                this.required = true;
                            } else {
                                this.required = false;
                            }
                        }
                    } else {
                        this.textToSort = this.dummyDataList[this.counter][this.displayMember];
                        if (this.isObject) {
                            this.value = this.dummyDataList[this.counter];
                        } else {
                            this.value = this.dummyDataList[this.counter][this.valueName];
                        }
                        this.selectRow = this.dummyDataList[this.counter];
                        if (this.isRequired) {
                            if (this.value) {
                                this.required = false;
                            } else {
                                this.required = true;
                            }
                        }
                    }
                }
            }
            if (event.keyCode === KEY_CODE.DOWN_ARROW) {
                this.showDropDown = true;
                this.counter = this.counter === this.dummyDataList.length - 1 ? this.counter : ++this.counter;
                this.checkHighlight(this.counter);
                if (this.displayMember) {
                    if (this.getValueDisplay) {
                        this.textToSort = this.dummyDataList[this.counter][this.displayMember];
                        this.value = this.dummyDataList[this.counter][this.displayMember];
                        if (this.isRequired) {
                            if (!this.value) {
                                this.required = true;
                            } else {
                                this.required = false;
                            }
                        }
                    } else {
                        this.textToSort = this.dummyDataList[this.counter][this.displayMember];
                        if (this.isObject) {
                            this.value = this.dummyDataList[this.counter];
                        } else {
                            this.value = this.dummyDataList[this.counter][this.valueName];
                        }
                        this.selectRow = this.dummyDataList[this.counter];
                        if (this.isRequired) {
                            if (this.value) {
                                this.required = false;
                            } else {
                                this.required = true;
                            }
                        }
                    }
                }
            }
        } else {
            if (event.keyCode === KEY_CODE.UP_ARROW) {
                this.showDropDown = true;
                this.counter = this.counter === 0 ? this.counter : --this.counter;
                this.checkHighlight(this.counter);
                if (this.displayMember) {
                    if (this.getValueDisplay) {
                        this.textToSort = this.dataList[this.counter][this.displayMember];
                        this.value = this.dataList[this.counter][this.displayMember];
                        if (this.isRequired) {
                            if (!this.value) {
                                this.required = true;
                            } else {
                                this.required = false;
                            }
                        }
                    } else {
                        this.textToSort = this.dataList[this.counter][this.displayMember];
                        if (this.isObject) {
                            this.value = this.dataList[this.counter];
                        } else {
                            this.value = this.dataList[this.counter][this.valueName];
                        }
                        this.selectRow = this.dataList[this.counter];
                        if (this.isRequired) {
                            if (this.value) {
                                this.required = false;
                            } else {
                                this.required = true;
                            }
                        }
                    }
                }
            }
            if (event.keyCode === KEY_CODE.DOWN_ARROW) {
                this.showDropDown = true;
                this.counter = this.counter === this.dataList.length - 1 ? this.counter : ++this.counter;
                this.checkHighlight(this.counter);
                if (this.displayMember) {
                    if (this.getValueDisplay) {
                        this.textToSort = this.dataList[this.counter][this.displayMember];
                        this.value = this.dataList[this.counter][this.displayMember];
                        if (this.isRequired) {
                            if (!this.value) {
                                this.required = true;
                            } else {
                                this.required = false;
                            }
                        }
                    } else {
                        this.textToSort = this.dataList[this.counter][this.displayMember];
                        if (this.isObject) {
                            this.value = this.dataList[this.counter];
                        } else {
                            this.value = this.dataList[this.counter][this.valueName];
                        }
                        this.selectRow = this.dataList[this.counter];
                        if (this.isRequired) {
                            if (this.value) {
                                this.required = false;
                            } else {
                                this.required = true;
                            }
                        }
                    }
                }
            }
        }*/
        // if(event.keyCode === KEY_CODE.TAB_KEY){
        //   this.textToSort = this.dataList[this.counter];
        //   this.showDropDown = false;
        // }
    }

    checkHighlight(currentItem): boolean {
        if (this.counter === currentItem) {
            return true;
        } else {
            return false;
        }
    }

    // onTabButtonAction(event: KeyboardEvent):void{
    //   console.log('event.keyCode',event.keyCode);
    // }

    constructor(
        private comboBoxPipe: ComboBoxPipe,
        private utilsService: UtilsService,
        private toastr: ToastrService,
        private translate: TranslateService,
        private comboboxModalService: ComboboxModalService
    ) {
        this.reset();
    }

    ngOnInit() {
        this.required = false;
        this.reset();
        this.zindexForDropdown = this.isOutTable;
        if (this.dataList) {
            if (this.dataList.length > 0) {
                if (this.dataList[0].hasOwnProperty('isParentNode')) {
                    this.configComboboxWithParentNode(this.dataList.filter(n => n['isParentNode'] && n['grade'] === 1), 1);
                }
            }
        }
        // this.isOutTable = true; // Hautv edit
    }

    // Add by Hautv
    configComboboxWithParentNode(dataPerent: any[], i): void {
        const dataParent: any[] = dataPerent.filter(n => n['isParentNode'] && n['grade'] === i);
        dataParent.forEach(n => {
            n.space = i;
            const children = this.dataList.filter(n => n['parentID'] === n['id']);
            if (children) {
                this.configComboboxWithParentNode(children, i++);
            }
        });
    }

    ngOnChanges(changes: SimpleChanges): void {
        if (this.getValueDisplay) {
            // this.textToSort = this.value;
            if (this.isRequired) {
                if (!this.textToSort) {
                    this.required = true;
                } else {
                    this.required = false;
                }
            }
        } else {
            if (this.value || this.value === 0) {
                if (this.isObject) {
                    this.selectRow = this.value;
                    if (this.displayMember) {
                        this.textToSort = this.value[this.displayMember];
                    }
                } else {
                    for (const r of this.dataList) {
                        if (r[this.valueName] || r[this.valueName] === 0) {
                            if (this.valueIsNumber) {
                                if (r[this.valueName] === this.value) {
                                    this.selectRow = r;
                                    break;
                                }
                            } else {
                                if (String(r[this.valueName]).toUpperCase() === String(this.value).toUpperCase()) {
                                    this.selectRow = r;
                                    break;
                                }
                            }
                        }
                    }
                    if (this.selectRow) {
                        if (this.displayMember) {
                            this.textToSort = this.selectRow[this.displayMember];
                        }
                    } else {
                        this.textToSort = this.value;
                    }
                }
            } else {
                this.textToSort = null;
                this.required = false;
            }
        }
    }

    focusText(event) {
        // Hautv comment
        /*this.dummyDataList = this.seachWithText(this.textToSort);
        if (!this.showDropDown) {
            this.showDropDown = true;
        }
        if (event.clientX > 1200) {
            this.overFlow = true;
        }*/
    }

    toogleDropDown(event): void {
        this.dummyDataList = this.dataList;
        this.showDropDown = !this.showDropDown;
        // prevent overflow page
        if (window.innerWidth - event.clientX < 400) {
            this.overFlow = true;
        }
        if (event.clientY > 260) {
            this.isOverBottomPage = true;
        }
        /*Add by hautv*/
        if (this.nameField) {
            this.setLocaitonDropDown(this.nameField, event);
        } else if (this.nameField_Number) {
            this.setLocaitonDropDown(this.nameField_Number, event);
        } else if (this.nameField_NumberIsRateType) {
            this.setLocaitonDropDown(this.nameField, event);
        }
        if (this.showDropDown && this.dataList) {
            setTimeout(() => this.onScrollToItem(), 100);
        }
    }

    onScrollToItem() {
        // class table
        const parentContainer = document.getElementsByClassName('dropdown-element')[0];
        if (parentContainer === undefined) {
            return;
        }
        // class item selected
        const element = parentContainer.getElementsByClassName('selected')[0];
        if (element === undefined) {
            return;
        }
        const elRect = element.getBoundingClientRect(),
            parRect = parentContainer.getBoundingClientRect();
        const elementHeight = elRect.height;
        if (!(elRect.top >= parRect.top && elRect.bottom <= parRect.bottom && elRect.bottom + elementHeight <= parRect.bottom)) {
            parentContainer.scrollTop = parentContainer.scrollTop + elRect.height + (elRect.bottom - parRect.bottom);
        }
    }

    /*Add by hautv*/
    setLocaitonDropDown(nameField, event) {
        let nF = nameField.nativeElement.parentElement;
        let checkInsideTable;
        for (let i = 0; i < 10; i++) {
            nF = nF.parentElement;
            if (nF.nodeName.includes('TABLE')) {
                checkInsideTable = true;
                break;
            }
        }
        if (checkInsideTable) {
            // const pTb = nF.getBoundingClientRect();
            const pTb_Div = nF.parentElement.getBoundingClientRect();
            const long = pTb_Div.y + pTb_Div.height - event.clientY;
            if (long < 135) {
                this.bottom100 = true;
                // this.isOutTable = true;
            } else {
                this.bottom100 = false;
                // this.isOutTable = false;
                this.zindexForDropdown = this.isOutTable;
            }
        }
    }

    reset(): void {
        if (this.dataList === undefined) {
            this.dataList = [];
        }
        this.showDropDown = false;
        this.dummyDataList = this.dataList;
    }

    textChange(value) {
        this.dummyDataList = [];
        if (this.getValueDisplay) {
            if (value || value === 0) {
                this.value = value;
                this.dummyDataList = this.seachWithText(value);
                this.required = false;
            } else {
                if (this.isRequired) {
                    this.required = true;
                }
                this.value = null;
            }
        } else {
            if (value.length > 0) {
                // this.dummyDataList = this.comboBoxPipe.transform(this.dataList, this.displayMember, value);
                this.dummyDataList = this.seachWithText(value);
                // console.log('this.dummyDataList',this.dummyDataList);
                if (this.dummyDataList) {
                    if (this.dummyDataList.length > 0) {
                        this.showDropDown = true;
                        if (this.isObject) {
                            if (this.dummyDataList[0][this.displayMember]) {
                                if (this.valueIsNumber) {
                                    if (isNumber(value)) {
                                        if (this.dummyDataList[0][this.displayMember] === value) {
                                            this.value = this.dummyDataList[0];
                                            this.selectRow = this.dummyDataList[0];
                                            this.textToSort = this.selectRow[this.displayMember];
                                            this.required = false;
                                        } else {
                                            this.value = null;
                                            this.selectRow = null;
                                            if (this.textToSort) {
                                                this.required = true;
                                            }
                                        }
                                    } else {
                                        this.value = null;
                                        this.selectRow = null;
                                        if (this.textToSort) {
                                            this.required = true;
                                        }
                                    }
                                } else {
                                    for (let i = 0; i < this.dummyDataList.length; i++) {
                                        if (
                                            String(this.dummyDataList[i][this.displayMember]).toUpperCase() ===
                                            value.toString().toUpperCase()
                                        ) {
                                            this.value = this.dummyDataList[i];
                                            this.selectRow = this.dummyDataList[i];
                                            this.textToSort = this.selectRow[this.displayMember];
                                            this.required = false;
                                            break;
                                        } else {
                                            this.value = null;
                                            this.selectRow = null;
                                            if (this.textToSort) {
                                                this.required = true;
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            for (let i = 0; i < this.dummyDataList.length; i++) {
                                if (this.dummyDataList[i][this.displayMember]) {
                                    if (this.valueIsNumber) {
                                        if (isNumber(value)) {
                                            if (this.dummyDataList[i][this.displayMember] === value) {
                                                this.value = this.dummyDataList[i][this.valueName];
                                                this.selectRow = this.dummyDataList[i];
                                                this.textToSort = this.selectRow[this.displayMember];
                                                this.required = false;
                                                break;
                                            } else {
                                                this.value = null;
                                                this.selectRow = null;
                                                if (this.textToSort) {
                                                    this.required = true;
                                                }
                                            }
                                        } else {
                                            this.value = null;
                                            this.selectRow = null;
                                            if (this.textToSort) {
                                                this.required = true;
                                            }
                                        }
                                    } else {
                                        if (
                                            String(this.dummyDataList[i][this.displayMember]).toUpperCase() ===
                                            value.toString().toUpperCase()
                                        ) {
                                            this.value = this.dummyDataList[i][this.valueName];
                                            this.selectRow = this.dummyDataList[i];
                                            this.textToSort = this.selectRow[this.displayMember];
                                            this.required = false;
                                            break;
                                        } else {
                                            this.value = null;
                                            this.selectRow = null;
                                            if (this.textToSort) {
                                                this.required = true;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        this.value = null;
                    }
                }
            } else {
                this.value = null;
                this.required = false;
                this.reset();
            }
        }
        if (this.clientY && this.clientX) {
            // prevent overflow page
            if (window.innerWidth - this.clientX < 400) {
                this.overFlow = true;
            }
            if (this.clientY > 260) {
                this.isOverBottomPage = true;
            }
            /*Add by hautv*/
            if (this.nameField) {
                this.setLocaitonDropDown(this.nameField, event);
            } else if (this.nameField_Number) {
                this.setLocaitonDropDown(this.nameField_Number, event);
            } else if (this.nameField_NumberIsRateType) {
                this.setLocaitonDropDown(this.nameField, event);
            }
        }
    }

    seachWithText(text: string) {
        let listSeach: any[];
        listSeach = [];
        for (let i = 0; i < this.dataList.length; i++) {
            for (let j = 0; j < this.listColumns.length; j++) {
                if (text) {
                    if (
                        String(this.dataList[i][this.listColumns[j]] === null ? '' : this.dataList[i][this.listColumns[j]])
                            .toString()
                            .toUpperCase()
                            .includes(text.toString().toUpperCase())
                    ) {
                        listSeach.push(this.dataList[i]);
                        break;
                    }
                } else {
                    listSeach.push(this.dataList[i]);
                    break;
                }
            }
        }
        return listSeach;
    }

    updateTextBox(valueSelected) {
        if (this.noGetValueParentNode && valueSelected['isParentNode']) {
            this.value = null;
            this.textToSort = '';
            this.toastr.warning('Không hạch toán vào chỉ tiêu cha');
            return;
        }
        if (this.getValueDisplay) {
            if (this.displayMember) {
                if (this.isRateType) {
                    this.value = valueSelected[this.valueName];
                    this.textToSort = valueSelected[this.displayMember];
                } else {
                    this.value = valueSelected[this.displayMember];
                    this.textToSort = valueSelected[this.displayMember];
                }
                if (this.isRequired) {
                    if (!this.textToSort) {
                        this.required = true;
                    } else {
                        this.required = false;
                    }
                }
                this.showDropDown = false;
                this.inSide = false;
            }
        } else {
            this.selectRow = valueSelected;
            if (this.displayMember) {
                this.textToSort = valueSelected[this.displayMember];
            }
            if (this.isObject) {
                this.value = valueSelected;
            } else {
                this.value = valueSelected[this.valueName];
            }
            this.inSide = false;
            this.showDropDown = false;
            this.required = false;
        }
    }

    onSelect(select: any) {
        this.selectRow = select;
        console.log(`selectedTest1 = ${JSON.stringify(this.selectRow)}`);
        // alert(`selectedTest1 = ${JSON.stringify(this.selectedTest1)}`);
    }

    Add() {
        this.comboboxModalService.isOpen = false;
        this.utilsService.setShowPopup(true);
        this.modalRef = this.comboboxModalService.open(this.nameCategory, this.typeObject, this.isCostSet);
    }

    getDate(date) {
        if (date) {
            return moment(date, DATE_FORMAT).format('DD/MM/YYYY');
        }
    }

    getData(nameColumn, value) {
        if (nameColumn.toLowerCase().includes('date')) {
            return this.getDate(value[nameColumn]);
        } else {
            return value[nameColumn];
        }
    }

    clickInside() {
        try {
            this.nameField.nativeElement.focus();
        } catch (e) {}
    }

    tab() {
        this.inSide = false;
        this.showDropDown = false;
    }
}
