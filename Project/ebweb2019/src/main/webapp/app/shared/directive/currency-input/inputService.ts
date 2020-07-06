import { InputManager } from './inputManager';

export class InputService {
    private inputManager: InputManager;

    constructor(private htmlInputElement: any, private options: any) {
        this.inputManager = new InputManager(htmlInputElement);
    }

    addNumber(keyCode: number): void {
        if (!this.rawValue) {
            this.rawValue = this.applyMask(false, '0');
        }

        const keyChar = String.fromCharCode(keyCode);
        const selectionStart = this.inputSelection.selectionStart;
        const selectionEnd = this.inputSelection.selectionEnd;
        this.rawValue = this.rawValue.substring(0, selectionStart) + keyChar + this.rawValue.substring(selectionEnd, this.rawValue.length);
        this.updateFieldValue(selectionStart + 1);
    }

    getDefaultPrecision() {
        return this.options.decimal + '0'.repeat(this.options.precision);
    }

    set value(value: number) {
        let valueString = '0';
        value = value ? value : 0;
        if (value || value === 0) {
            value = this.round(value, this.options.precision);
            valueString = value.toString().replace('.', this.options.decimal);
            if (value % 1 !== 0) {
                const decimalPart = (valueString += '0'.repeat(this.options.precision));
                const valueStringArray = decimalPart.split(this.options.decimal);
                valueString = valueStringArray.join(this.options.decimal);
            }
        }
        this.rawValue = this.applyMask(false, valueString ? valueString : '' + value);
    }

    clearMask(rawValue: string): number {
        let value = (rawValue || '0').replace(this.options.prefix, '').replace(this.options.suffix, '');

        if (this.options.thousands) {
            value = value.replace(new RegExp('\\' + this.options.thousands, 'g'), '');
        }

        if (this.options.decimal) {
            value = value.replace(this.options.decimal, '.');
        }
        if (this.options.negative === '(') {
            value = value.replace('(', '-');
            value = value.replace(')', '');
        }
        return this.round(parseFloat(value), this.options.precision);
    }

    round(value, decimals) {
        return Number(Math.round(parseFloat(`${value}e${decimals}`)) + 'e-' + decimals);
    }

    changeToNegative(currentIndex: number): void {
        if (this.options.allowNegative && this.rawValue !== '' && this.rawValue.charAt(0) !== this.options.negative && this.value !== 0) {
            if (this.options.negative === '(') {
                this.rawValue = `(${this.rawValue})`;
            } else {
                this.rawValue = '-' + this.rawValue;
            }
            this.updateCursor(currentIndex + 1);
        }
    }

    changeToPositive(currentIndex): void {
        if (this.options.negative === '(') {
            this.rawValue = this.rawValue.replace('(', '');
            this.rawValue = this.rawValue.replace(')', '');
        } else {
            this.rawValue = this.rawValue.replace('-', '');
        }
        this.updateCursor(currentIndex - 1);
    }

    removeNumber(keyCode: number): void {
        let selectionEnd = this.inputSelection.selectionEnd;
        let selectionStart = this.inputSelection.selectionStart;

        if (selectionStart > this.rawValue.length - this.options.suffix.length) {
            selectionEnd = this.rawValue.length - this.options.suffix.length;
            selectionStart = this.rawValue.length - this.options.suffix.length;
        }

        selectionEnd = keyCode === 46 || keyCode === 63272 ? selectionEnd + 1 : selectionEnd;
        selectionStart = keyCode === 8 ? selectionStart - 1 : selectionStart;
        this.rawValue = this.rawValue.substring(0, selectionStart) + this.rawValue.substring(selectionEnd, this.rawValue.length);
        this.updateFieldValue(selectionStart);
    }

    updateFieldValue(selectionStart?: number): void {
        const newRawValue = this.applyMask(false, this.rawValue || '');
        selectionStart = selectionStart === undefined ? this.rawValue.length : selectionStart;
        this.inputManager.updateValueAndCursor(newRawValue, this.rawValue.length, selectionStart);
    }

    updateOptions(options: any): void {
        this.options = options;
        const value: number = parseFloat(this.value.toFixed(this.options.decimal));
        this.value = value;
    }

    get canInputMoreNumbers(): boolean {
        return this.inputManager.canInputMoreNumbers;
    }

    get inputSelection(): any {
        return this.inputManager.inputSelection;
    }

    get rawValue(): string {
        return this.inputManager.rawValue;
    }

    set rawValue(value: string) {
        this.inputManager.rawValue = value;
    }

    get storedRawValue(): string {
        return this.inputManager.storedRawValue;
    }

    get value(): number {
        return this.clearMask(this.rawValue);
    }

    applyMask(isNumber: boolean, rawValue: string): string {
        const { allowNegative, decimal, precision, prefix, suffix, thousands } = this.options;
        const defaultPrecision = this.getDefaultPrecision();
        rawValue = isNumber ? Number(rawValue).toFixed(precision) : rawValue;
        const valueArray = rawValue.split(decimal);
        const integerPart = valueArray[0]
            .replace(/^0*/g, '')
            .replace(/\D/g, '')
            .replace(/\B(?=(\d{3})+(?!\d))/g, thousands);
        let newRawValue = integerPart || '0';
        let decimalPart;
        if (!valueArray[1]) {
            decimalPart = precision ? defaultPrecision : '';
        } else {
            decimalPart = decimal + valueArray[1].slice(0, precision);
        }
        newRawValue += decimalPart;
        const isZero = parseInt(integerPart, 10) === 0 && (parseInt(decimalPart, 10) === 0 || decimalPart === '');
        let operator = '';
        if (this.options.negative === '(' && (rawValue.indexOf('(') > -1 || rawValue.indexOf('-') > -1) && allowNegative && !isZero) {
            return '(' + prefix + newRawValue + suffix + ')';
        } else {
            operator = rawValue.indexOf('-') > -1 && allowNegative && !isZero ? '-' : '';
        }
        return operator + prefix + newRawValue + suffix;
    }

    changeToDecimal() {
        const { decimal, precision } = this.options;
        if (precision) {
            this.inputManager.updateValueAndCursor(this.rawValue, this.rawValue.length, this.rawValue.indexOf(decimal) + 1);
        }
    }

    updateCursor(position) {
        this.inputManager.updateValueAndCursor(this.rawValue, this.rawValue.length, position);
    }
}
