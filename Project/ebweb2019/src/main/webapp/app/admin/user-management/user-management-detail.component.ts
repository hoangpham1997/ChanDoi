import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { User } from 'app/core';

@Component({
    selector: 'eb-user-mgmt-detail-admin',
    templateUrl: './user-management-detail.component.html'
})
export class UserMgmtDetailAdminComponent implements OnInit {
    user: User;

    constructor(private route: ActivatedRoute) {}

    ngOnInit() {
        this.route.data.subscribe(({ user }) => {
            this.user = user.body ? user.body : user;
        });
    }
}
