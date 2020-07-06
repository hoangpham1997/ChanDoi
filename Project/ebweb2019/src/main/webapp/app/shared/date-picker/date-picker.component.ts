import { Component, ElementRef, EventEmitter, forwardRef, Input, OnInit, Output, ViewChild } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import * as moment from 'moment';
import { DATE_FORMAT_SLASH, DATE_TIME_FORMAT, DATE_TIME_SECOND_FORMAT } from 'app/shared';

const noop = () => {};

export const CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR: any = {
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => DatePickerComponent),
    multi: true
};

@Component({
    selector: 'eb-date-picker',
    templateUrl: './date-picker.component.html',
    styleUrls: ['./date-picker.component.css'],
    providers: [CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR]
})
export class DatePickerComponent implements OnInit, ControlValueAccessor {
    private innerValue: any = '';
    @Input() mask: string;
    @Input() format: string;
    @Input() disabled: boolean;
    @Input() isInTable: boolean;
    @Input() isMultiTabs: boolean;
    @Input() minDate: any;
    @Input() maxDate: any;
    @Input() isRequired: boolean;
    @Input() isTime: boolean;
    @Input() isSecond: boolean;
    @Input() tabindex: boolean;
    @Input() backgroundInherit: boolean;
    @Input() idIp: string;
    @Input() nameIp: string;
    @Input() showToptoggleDropDown: boolean;
    @Output() focusInput = new EventEmitter<any>();
    date: string;
    dateHolder: any;
    showTime: boolean;
    time: any;

    private onTouchedCallback: () => void = noop;
    private onChangeCallback: (_: any) => void = noop;
    overFlow: boolean;
    isOverBottomPage: boolean;
    bottom100: boolean;
    isOutTable: boolean;
    zIndexForDropdown: boolean;
    @ViewChild('nameOfInputCombobox') nameField: ElementRef; // sử dụng kick chuột trong vùng div mà không có dữ liệu

    // get accessor
    get value(): any {
        return this.innerValue;
    }

    // set accessor including call the onchange callback
    set value(v: any) {
        if (!v) {
            this.innerValue = null;
        }
        let value = v;
        let oldInnerValue = this.innerValue;
        if (this.format) {
            value = moment(v, 'DD/MM/YYYY').format(this.format);
            if (oldInnerValue) {
                oldInnerValue = moment(this.innerValue, 'DD/MM/YYYY').format(this.format);
            }
        } else {
            if (this.isTime) {
                if (this.isSecond) {
                    value = moment(`${v} ${this.time.hour}:${this.time.minute}:${this.time.second}`, 'DD/MM/YYYY HH:mm:ss').format(
                        DATE_TIME_SECOND_FORMAT
                    );
                    if (oldInnerValue) {
                        oldInnerValue = moment(
                            `${this.innerValue} ${this.time.hour}:${this.time.minute}:${this.time.second}`,
                            'DD/MM/YYYY HH:mm:ss'
                        ).format(DATE_TIME_SECOND_FORMAT);
                    }
                } else {
                    value = moment(`${v} ${this.time.hour}:${this.time.minute}`, 'DD/MM/YYYY HH:mm').format(DATE_TIME_FORMAT);
                    if (oldInnerValue) {
                        oldInnerValue = moment(`${this.innerValue} ${this.time.hour}:${this.time.minute}`, 'DD/MM/YYYY HH:mm').format(
                            DATE_TIME_FORMAT
                        );
                    }
                }
            } else {
                value = moment(v, 'DD/MM/YYYY').format(DATE_FORMAT_SLASH);
                if (oldInnerValue) {
                    oldInnerValue = moment(this.innerValue, 'DD/MM/YYYY').format(DATE_FORMAT_SLASH);
                }
            }
        }
        if (value !== oldInnerValue) {
            if (v && v.length === 10) {
                try {
                    if (this.format) {
                        this.innerValue = moment(v, 'DD/MM/YYYY').format(this.format);
                    } else {
                        if (this.isTime) {
                            if (this.isSecond) {
                                this.innerValue = moment(
                                    `${v} ${this.time.hour}:${this.time.minute}:${this.time.second}`,
                                    'DD/MM/YYYY HH:mm:ss'
                                );
                            } else {
                                this.innerValue = moment(`${v} ${this.time.hour}:${this.time.minute}`, 'DD/MM/YYYY HH:mm');
                            }
                        } else {
                            this.innerValue = moment(v, 'DD/MM/YYYY');
                        }
                    }
                } catch (e) {
                    this.innerValue = null;
                }
            } else {
                this.innerValue = null;
                this.dateHolder = null;
            }

            this.onChangeCallback(this.innerValue);
        }
    }

    ngOnInit(): void {
        this.mask = this.mask ? this.mask : '00/00/0000';
        if (this.isTime) {
            const date = moment();
            if (!this.isSecond) {
                this.time = { hour: date.hour(), minute: date.minute() };
            } else {
                this.time = { hour: date.hour(), minute: date.minute(), second: date.second() };
            }
        }
    }

    registerOnChange(fn: any): void {
        this.onChangeCallback = fn;
    }

    registerOnTouched(fn: any): void {
        this.onTouchedCallback = fn;
    }

    setDisabledState(isDisabled: boolean): void {
        this.disabled = isDisabled;
    }

    writeValue(value: any): void {
        if (this.isMultiTabs && this.date === '' && value !== '') {
            if (this.isTime || this.isSecond) {
                this.time = { hour: moment(value).hour(), minute: moment(value).minute(), second: moment(value).second() };
            }
            this.date = value;
            return;
        }
        this.dateHolder = null;
        if (value !== this.innerValue) {
            this.innerValue = value;
            if (this.innerValue instanceof moment) {
                if (this.isTime || this.isSecond) {
                    this.time = { hour: this.innerValue.hour(), minute: this.innerValue.minute(), second: this.innerValue.second() };
                }
                this.date = value.format('DD/MM/YYYY');
                if (this.format) {
                    this.innerValue = value.format(this.format);
                }
            } else {
                if (this.format) {
                    const date = moment(value, this.format);
                    if (date.isValid()) {
                        this.innerValue = this.date = moment(value, this.format).format('DD/MM/YYYY');
                    } else if (!this.innerValue) {
                        this.date = '';
                    }
                } else {
                    if (this.isTime || this.isSecond) {
                        this.time = { hour: moment(value).hour(), minute: moment(value).minute(), second: moment(value).second() };
                    }
                    this.date = this.innerValue = value;
                }
            }
        } else {
            if (this.isTime || this.isSecond) {
                this.time = { hour: moment(value).hour(), minute: moment(value).minute(), second: moment(value).second() };
            }
            this.dateHolder = value;
        }
    }

    update() {
        // this.innerValue = this.dateHolder.format('DD/MM/YYYY');
        this.date = this.dateHolder.format('DD/MM/YYYY');
        this.value = this.date;
    }

    parseDate() {
        const beforeParse = this.date.replace(new RegExp('/', 'g'), '');
        if (beforeParse.length === 8) {
            try {
                const data = moment(beforeParse, 'DDMMYYYY').format('DD/MM/YYYY');
                if (data !== 'Invalid date') {
                    this.date = data;
                }
            } catch (e) {
                this.date = '';
            }
        }
        if (this.isMultiTabs && this.date === '' && this.value !== '') {
            this.date = this.value;
        }

        this.value = this.date;
    }

    checkDate() {
        if (!this.date || this.date.length !== 10) {
            this.date = null;
        } else {
            const beforeParse = this.date.replace(new RegExp('/', 'g'), '');
            const data = moment(beforeParse, 'DDMMYYYY').format('DD/MM/YYYY');
            const date = moment(beforeParse, 'DDMMYYYY');
            if (data === 'Invalid date' || !date.isValid() || date.year() < 1920) {
                this.date = null;
            }
        }
    }

    toggleDropDown(event): void {
        this.showTime = !this.showTime;
        // prevent overflow page
        if (event.clientX > 1200) {
            this.overFlow = true;
        }
        if (event.clientY > 260) {
            this.isOverBottomPage = true;
        }
        this.setLocationDropDown(this.nameField, event);
    }

    setLocationDropDown(nameField, event) {
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
            if (long < 180) {
                this.bottom100 = true;
                this.isOutTable = true;
            } else {
                this.bottom100 = false;
                this.zIndexForDropdown = this.isOutTable;
            }
        }
    }

    focus(event) {
        this.focusInput.emit(event);
    }
}
