import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMaterialGoodsAssembly } from 'app/shared/model/material-goods-assembly.model';

@Component({
    selector: 'eb-material-goods-assembly-detail',
    templateUrl: './material-goods-assembly-detail.component.html'
})
export class MaterialGoodsAssemblyDetailComponent implements OnInit {
    materialGoodsAssembly: IMaterialGoodsAssembly;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ materialGoodsAssembly }) => {
            this.materialGoodsAssembly = materialGoodsAssembly;
        });
    }

    previousState() {
        window.history.back();
    }
}
