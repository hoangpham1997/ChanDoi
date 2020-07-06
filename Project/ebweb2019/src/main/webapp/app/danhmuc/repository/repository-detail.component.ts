import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRepository } from 'app/shared/model/repository.model';

@Component({
    selector: 'eb-repository-detail',
    templateUrl: './repository-detail.component.html'
})
export class RepositoryDetailComponent implements OnInit {
    repository: IRepository;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ repository }) => {
            this.repository = repository;
        });
    }

    previousState() {
        window.history.back();
    }
}
