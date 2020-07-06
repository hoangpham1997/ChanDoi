import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMaterialGoodsSpecifications } from 'app/shared/model/material-goods-specifications.model';

@Component({
    selector: 'eb-material-goods-specifications-detail',
    templateUrl: './material-goods-specifications-detail.component.html'
})
export class MaterialGoodsSpecificationsDetailComponent implements OnInit {
    materialGoodsSpecifications: IMaterialGoodsSpecifications;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ materialGoodsSpecifications }) => {
            this.materialGoodsSpecifications = materialGoodsSpecifications;
        });
    }

    previousState() {
        window.history.back();
    }
}
