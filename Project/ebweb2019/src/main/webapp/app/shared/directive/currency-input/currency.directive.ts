import { Directive, ElementRef, forwardRef, HostListener, Input, OnChanges, SimpleChanges } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { InputHandler } from './inputHandler';
import {
    DDSo_DocTienLe,
    DDSo_DonGia,
    DDSo_DonGiaNT,
    DDSo_NCachHangDVi,
    DDSo_NCachHangNghin,
    DDSo_NgoaiTe,
    DDSo_SoAm,
    DDSo_SoLuong,
    DDSo_TienVND,
    DDSo_TyGia,
    DDSo_TyLe,
    DDSo_TyLePBo,
    DECIMAL,
    SO_NGUYEN,
    SUFFIX,
    THOUSANDS
} from 'app/app.constants';

export const CURRENCYMASKDIRECTIVE_VALUE_ACCESSOR: any = {
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => CurrencyMaskDirective),
    multi: true
};

@Directive({
    selector: '[ebCurrencyMask]',
    providers: [CURRENCYMASKDIRECTIVE_VALUE_ACCESSOR]
})
export class CurrencyMaskDirective implements ControlValueAccessor, OnChanges {
    @Input() options: any;
    @Input() systemOptions: any;
    @Input() type: number;
    @Input() allowNegative = true;
    @Input() textAlign: string;

    public inputHandler: InputHandler;
    private currentOnChangeFunction: Function;
    private currentOnTouchFunction: Function;

    public optionsTemplate = {
        align: 'right',
        allowNegative: true,
        allowZero: false,
        decimal: DECIMAL,
        precision: 0,
        prefix: '',
        suffix: SUFFIX,
        thousands: THOUSANDS,
        isDecimal: false,
        negative: '-',
        autocomplete: 'off',
        type: 'search'
    };

    constructor(private elementRef: ElementRef) {}

    ngOnChanges() {
        this.elementRef.nativeElement.style.textAlign =
            this.textAlign && ['left', 'right'].includes(this.textAlign) ? this.textAlign : 'right';

        // tắt autocomplete của trình duyệt
        this.elementRef.nativeElement.autocomplete = this.optionsTemplate.autocomplete;
        this.elementRef.nativeElement.type = this.optionsTemplate.type;

        // Truyền option cũ (object)
        let option = (<any>Object).assign({}, this.optionsTemplate, this.options);

        // Truyền option kiểu mới
        if (this.systemOptions && this.type) {
            option = this.getOptionFromSys(this.systemOptions, this.getStringFromType(this.type));
        }
        if (!this.allowNegative) {
            option.allowNegative = false;
        }
        this.inputHandler = new InputHandler(this.elementRef.nativeElement, option);
        setTimeout(() => {
            if (this.currentOnChangeFunction) {
                this.inputHandler.setOnModelChange(this.currentOnChangeFunction);
            }
            if (this.currentOnTouchFunction) {
                this.inputHandler.setOnModelTouched(this.currentOnTouchFunction);
            }
        }, 0);
    }

    getStringFromType(index: number) {
        const typeArray = [
            DDSo_DonGia,
            DDSo_DonGiaNT,
            DDSo_SoLuong,
            DDSo_TyGia,
            DDSo_TyLe,
            DDSo_TyLePBo,
            DDSo_TienVND,
            DDSo_NgoaiTe,
            SO_NGUYEN
        ];
        if (index < 0 || index > typeArray.length) {
            return typeArray[0];
        }
        return typeArray[index - 1];
    }

    getOptionFromSys(systemOption, type) {
        const option = {
            decimal: DECIMAL,
            thousands: THOUSANDS,
            negative: '-',
            precision: 0
        };

        for (const item of systemOption) {
            switch (item.code) {
                case DDSo_NCachHangNghin:
                    option.thousands = item.data;
                    break;
                case DDSo_NCachHangDVi:
                    option.decimal = item.data;
                    break;
                case DDSo_DocTienLe:
                    break;
                case DDSo_SoAm:
                    if (item.data === '0') {
                        option.negative = '(';
                    }
                    break;
                case SO_NGUYEN:
                    break;
                case type:
                    option.precision = parseInt(item.data, 10);
                    break;
            }
        }

        if (option.decimal === option.thousands) {
            option.decimal = DECIMAL;
            option.thousands = THOUSANDS;
        }

        return (<any>Object).assign({}, this.optionsTemplate, option);
    }

    @HostListener('blur', ['$event'])
    handleBlur(event: any) {
        this.inputHandler.getOnModelTouched().apply(event);
    }

    @HostListener('cut', ['$event'])
    handleCut(event: any) {
        if (!this.isChromeAndroid()) {
            this.inputHandler.handleCut(event);
        }
    }

    @HostListener('input', ['$event'])
    handleInput(event: any) {
        if (this.isChromeAndroid()) {
            this.inputHandler.handleInput(event);
        }
    }

    @HostListener('keydown', ['$event'])
    handleKeydown(event: any) {
        if (!this.isChromeAndroid()) {
            this.inputHandler.handleKeydown(event);
        }
    }

    @HostListener('keypress', ['$event'])
    handleKeypress(event: any) {
        if (!this.isChromeAndroid()) {
            this.inputHandler.handleKeypress(event);
        }
    }

    @HostListener('paste', ['$event'])
    handlePaste(event: any) {
        if (!this.isChromeAndroid()) {
            this.inputHandler.handlePaste(event);
        }
    }

    isChromeAndroid(): boolean {
        return /chrome/i.test(navigator.userAgent) && /android/i.test(navigator.userAgent);
    }

    registerOnChange(callbackFunction: Function): void {
        setTimeout(() => {
            if (!this.currentOnChangeFunction) {
                this.currentOnChangeFunction = callbackFunction;
                this.inputHandler.setOnModelChange(callbackFunction);
            }
        }, 0);
    }

    registerOnTouched(callbackFunction: Function): void {
        setTimeout(() => {
            if (!this.currentOnTouchFunction) {
                this.currentOnTouchFunction = callbackFunction;
                this.inputHandler.setOnModelTouched(callbackFunction);
            }
        }, 0);
    }

    setDisabledState(value: boolean): void {
        this.elementRef.nativeElement.disabled = value;
    }

    writeValue(value: number): void {
        this.inputHandler.setValue(value);
    }
}
