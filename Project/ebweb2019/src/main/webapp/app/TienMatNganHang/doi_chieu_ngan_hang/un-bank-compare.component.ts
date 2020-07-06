import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiAlertService } from 'ng-jhipster';

@Component({
    selector: 'eb-bank-compare',
    templateUrl: './un-bank-compare.component.html',
    styleUrls: ['./bank-compare.component.css']
})
export class UnBankCompareComponent implements OnInit {
    isSaving: boolean;

    constructor(private jhiAlertService: JhiAlertService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
    }

    previousState() {
        window.history.back();
    }
}
