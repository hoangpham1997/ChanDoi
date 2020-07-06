import { AfterViewInit, Component, EventEmitter, OnChanges, Output, SimpleChanges } from '@angular/core';

@Component({
    selector: 'eb-color-picker',
    templateUrl: './color-picker.component.html',
    styleUrls: ['./color-picker.component.css']
})
export class ColorPickerComponent {
    public hue: string;
    public color: string;

    @Output() selectedColor: EventEmitter<string> = new EventEmitter();

    colorChanged($event) {
        this.selectedColor.emit($event);
    }
}
