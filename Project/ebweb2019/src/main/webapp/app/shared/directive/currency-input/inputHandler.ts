import { InputService } from './inputService';

export class InputHandler {
    private inputService: InputService;
    private onModelChange: Function;
    private onModelTouched: Function;
    private options: any;

    constructor(htmlInputElement: HTMLInputElement, options: any) {
        this.inputService = new InputService(htmlInputElement, options);
        this.options = options;
        this.setValue(this.inputService.value);
    }

    handleCut(event: any): void {
        setTimeout(() => {
            this.inputService.updateFieldValue();
            this.setValue(this.inputService.value);
            this.onModelChange(this.inputService.value);
        }, 0);
    }

    handleInput(event: any): void {
        const keyCode = this.inputService.rawValue.charCodeAt(this.inputService.rawValue.length - 1);
        const rawValueLength = this.inputService.rawValue.length;
        const rawValueSelectionEnd = this.inputService.inputSelection.selectionEnd;
        const storedRawValueLength = this.inputService.storedRawValue.length;
        this.inputService.rawValue = this.inputService.storedRawValue;

        if (rawValueLength !== rawValueSelectionEnd || Math.abs(rawValueLength - storedRawValueLength) !== 1) {
            this.setCursorPosition(event);
            return;
        }

        if (rawValueLength < storedRawValueLength) {
            this.inputService.removeNumber(8);
        }

        if (rawValueLength > storedRawValueLength) {
            switch (keyCode) {
                case 43:
                    break;
                case 45:
                    const currentIndex = this.inputService.inputSelection.selectionStart;
                    if (this.inputService.rawValue && (this.inputService.rawValue[0] === '-' || this.inputService.rawValue[0] === '(')) {
                        this.inputService.changeToPositive(currentIndex);
                    } else {
                        this.inputService.changeToNegative(currentIndex);
                    }
                    break;
                default:
                    if (!this.inputService.canInputMoreNumbers) {
                        return;
                    }
                    this.inputService.addNumber(keyCode);
            }
        }

        this.setCursorPosition(event);
        setTimeout(() => {
            this.onModelChange(this.inputService.value);
        }, 1);
    }

    handleKeydown(event: any): void {
        const keyCode = event.which || event.charCode || event.keyCode;

        if (keyCode === 8 || keyCode === 46 || keyCode === 63272) {
            let startIndexCache;
            event.preventDefault();
            const selectionRangeLength = Math.abs(
                this.inputService.inputSelection.selectionEnd - this.inputService.inputSelection.selectionStart
            );

            if (!selectionRangeLength && this.inputService.inputSelection.selectionStart === 0 && keyCode === 8) {
                return;
            }

            if (selectionRangeLength >= this.inputService.rawValue.length || this.inputService.rawValue.length < 2) {
                this.setValue(0);
                startIndexCache = 1;
            } else if (selectionRangeLength === 0) {
                const decimalIndex = this.inputService.rawValue.indexOf(this.options.decimal);
                if (this.options.precision && this.inputService.inputSelection.selectionStart - 1 === decimalIndex && keyCode === 8) {
                    this.inputService.updateCursor(this.inputService.inputSelection.selectionStart - 1);
                    return;
                } else if (this.options.precision && this.inputService.inputSelection.selectionStart === decimalIndex && keyCode === 46) {
                    this.inputService.updateCursor(this.inputService.inputSelection.selectionStart + 1);
                    return;
                } else if (this.options.precision && this.inputService.inputSelection.selectionStart > decimalIndex && keyCode === 8) {
                    startIndexCache = this.inputService.inputSelection.selectionStart - 1;
                } else if (this.options.precision && this.inputService.inputSelection.selectionStart > decimalIndex && keyCode === 46) {
                    startIndexCache = this.inputService.inputSelection.selectionStart;
                } else if (this.options.precision && this.inputService.inputSelection.selectionStart < decimalIndex && keyCode === 46) {
                    if (this.inputService.rawValue[this.inputService.inputSelection.selectionStart] === this.options.thousands) {
                        startIndexCache = this.inputService.inputSelection.selectionStart + 1;
                    } else if (this.inputService.inputSelection.selectionStart === 0) {
                        if (this.inputService.inputSelection.selectionStart + 1 === decimalIndex) {
                            startIndexCache = decimalIndex + 1;
                        } else {
                            startIndexCache = 0;
                        }
                    } else {
                        startIndexCache = this.inputService.inputSelection.selectionStart;
                    }
                }
                this.inputService.removeNumber(keyCode);
            } else {
                startIndexCache = this.inputService.inputSelection.selectionStart;
                this.inputService.rawValue =
                    this.inputService.rawValue.slice(0, this.inputService.inputSelection.selectionStart) +
                    this.inputService.rawValue.slice(this.inputService.inputSelection.selectionEnd);
            }

            setTimeout(() => {
                this.inputService.updateFieldValue(
                    startIndexCache || startIndexCache === 0 ? startIndexCache : this.inputService.inputSelection.selectionStart
                );
                this.setValue(this.inputService.value);
                this.onModelChange(this.inputService.value);
                if (startIndexCache || startIndexCache === 0) {
                    this.inputService.updateCursor(startIndexCache);
                }
            }, 0);
        }
    }

    handleKeypress(event: any): void {
        const keyCode = event.which || event.charCode || event.keyCode;

        if (keyCode === 97 && event.ctrlKey) {
            return;
        }

        if (event.key === this.options.decimal || (event.key === '.' && this.options.decimal === ',')) {
            this.inputService.changeToDecimal();
        } else {
            switch (keyCode) {
                case undefined:
                case 9:
                case 13:
                case 37:
                case 39:
                    return;
                case 8:
                    break;
                case 43:
                    break;
                case 45:
                    const currentIndex = this.inputService.inputSelection.selectionStart;
                    if (this.inputService.rawValue && (this.inputService.rawValue[0] === '-' || this.inputService.rawValue[0] === '(')) {
                        this.inputService.changeToPositive(currentIndex);
                    } else {
                        this.inputService.changeToNegative(currentIndex);
                    }
                    break;
                default:
                    if (this.inputService.canInputMoreNumbers) {
                        const selectionRangeLength = Math.abs(
                            this.inputService.inputSelection.selectionEnd - this.inputService.inputSelection.selectionStart
                        );

                        if (selectionRangeLength === this.inputService.rawValue.length) {
                            this.setValue(0);
                            this.inputService.updateCursor(1);
                        }
                        this.inputService.addNumber(keyCode);
                        const decimalIndex = this.inputService.rawValue.indexOf(this.options.decimal);
                        if (this.options.precision && decimalIndex > 0 && this.inputService.inputSelection.selectionStart > decimalIndex) {
                            this.inputService.updateCursor(this.inputService.inputSelection.selectionStart + 1);
                        }
                    }
            }
        }

        event.preventDefault();
        setTimeout(() => {
            this.onModelChange(this.inputService.value);
        }, 0);
    }

    handlePaste(event: any): void {
        setTimeout(() => {
            this.inputService.updateFieldValue();
            this.setValue(this.inputService.value);
            this.onModelChange(this.inputService.value);
        }, 1);
    }

    updateOptions(options: any): void {
        this.inputService.updateOptions(options);
    }

    getOnModelChange(): Function {
        return this.onModelChange;
    }

    setOnModelChange(callbackFunction: Function): void {
        this.onModelChange = callbackFunction;
    }

    getOnModelTouched(): Function {
        return this.onModelTouched;
    }

    setOnModelTouched(callbackFunction: Function) {
        this.onModelTouched = callbackFunction;
    }

    setValue(value: number): void {
        this.inputService.value = value;
    }

    private setCursorPosition(event: any): void {
        setTimeout(() => {
            event.target.setSelectionRange(event.target.value.length, event.target.value.length);
        }, 0);
    }
}
