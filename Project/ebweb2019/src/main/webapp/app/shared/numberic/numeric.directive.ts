import { Directive, ElementRef, HostListener, Input } from '@angular/core';

@Directive({
    selector: '[ebNumeric]'
})
export class NumericDirective {
    @Input('decimals') decimals: number;
    // Số phần nguyên tối đa
    @Input('integers') integers: number;
    private specialKeys = ['Backspace', 'Tab', 'End', 'Home', 'ArrowLeft', 'ArrowRight', 'Delete'];

    constructor(private el: ElementRef) {}

    private check(value: string, decimals: number) {
        if (!decimals) {
            decimals = 0;
        }
        if (decimals <= 0) {
            return String(value).match(new RegExp(/^\d+$/));
        } else {
            const regExpString = '^\\s*((\\d+(\\.\\d{0,' + decimals + '})?)|((\\d*(\\.\\d{1,' + decimals + '}))))\\s*$';
            return String(value).match(new RegExp(regExpString));
        }
    }

    @HostListener('keydown', ['$event'])
    onKeyDown(event: KeyboardEvent) {
        if (this.specialKeys.indexOf(event.key) !== -1) {
            return;
        }
        // Do not use event.keycode this is deprecated.
        // See: https://developer.mozilla.org/en-US/docs/Web/API/KeyboardEvent/keyCode
        const current: string = this.el.nativeElement.value;

        if (current.length + 1 > this.integers) {
            event.preventDefault();
            return;
        }

        const next: string = current.concat(event.key);
        if (next && !this.check(next, this.decimals)) {
            event.preventDefault();
        }
    }
}
