import { Component, forwardRef, Input, OnInit } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import * as moment from 'moment';
import { Principal } from 'app/core';

const noop = () => {};

export const CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR: any = {
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => InputHintComponent),
    multi: true
};

@Component({
    selector: 'eb-input-hint',
    templateUrl: './input-hint.component.html',
    styleUrls: ['./input-hint.component.css'],
    providers: [CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR]
})
export class InputHintComponent implements OnInit, ControlValueAccessor {
    private innerValue: any = '';
    @Input() mask: string;
    @Input() format: string;
    @Input() disabled: boolean;
    @Input() isInTable: boolean;
    @Input() isMultiTabs: boolean;
    @Input() minDate: any;
    @Input() maxDate: any;
    @Input() isRequired: boolean;
    @Input() isLabel: boolean;
    @Input() labelTranslate: string;
    @Input() isNumber: boolean;
    ngModel: string;
    date: string;
    dateHolder: any;
    currentAccount: any;
    typeHTTT: any;
    private onTouchedCallback: () => void = noop;
    private onChangeCallback: (_: any) => void = noop;

    constructor(private principal: Principal) {
        this.principal.identity().then(account => {
            this.currentAccount = account;
            this.typeHTTT = this.currentAccount.systemOption.find(x => x.code === 'TCKHAC_HienThiThongTin' && x.data).data;
        });
    }

    // get accessor
    get value(): any {
        return this.innerValue;
    }

    // set accessor including call the onchange callback
    set value(v: any) {
        if (v !== this.innerValue) {
            this.innerValue = v;
            this.onChangeCallback(v);
        }
    }

    ngOnInit(): void {}

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
        if (this.isNumber) {
        } else {
            if (value) {
                this.ngModel = value;
            }
        }
    }

    update() {
        // this.innerValue = this.dateHolder.format('DD/MM/YYYY');
        this.ngModel = this.dateHolder.format('DD/MM/YYYY');
        this.value = this.ngModel;
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
            if (data === 'Invalid date') {
                this.date = null;
            }
        }
    }

    textChange() {
        if (this.isNumber) {
        } else {
            this.value = this.ngModel;
        }
    }
}
