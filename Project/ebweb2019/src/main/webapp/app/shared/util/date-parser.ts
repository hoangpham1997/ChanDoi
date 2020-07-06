import { Injectable } from '@angular/core';
import { NgbDateParserFormatter, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';

@Injectable()
export class NgbDateFRParserFormatter extends NgbDateParserFormatter {
    parse(value: string): NgbDateStruct {
        // parse receive your string dd/mm/yyy
        // return a NgbDateStruct
        // calculate year,month and day from "value"
        // edit by anmt
        return { year: new Date().getFullYear(), month: new Date().getMonth() + 1, day: new Date().getDate() };
    }

    format(date: NgbDateStruct): string {
        // receive a NgbDateStruct
        // return a string
        let strDay = '';
        let strMonth = '';
        if (!date) {
            return null;
            // edit by mran
            // date = { year: new Date().getFullYear(), month: (new Date()).getMonth()+1, day: new Date().getDate() };
        }
        strDay = date.day < 10 ? '0' + date.day : '' + date.day;
        strMonth = date.month < 10 ? '0' + date.month : '' + date.month;
        return strDay + '/' + strMonth + '/' + date.year;
    }
}
