import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { User, UserService } from 'app/core';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';

@Component({
    selector: 'eb-user-mgmt-delete-dialog-admin',
    templateUrl: './user-management-delete-dialog.component.html'
})
export class UserMgmtDeleteDialogAdminComponent {
    _user: User;

    constructor(
        private userService: UserService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager,
        private toastr: ToastrService,
        private translate: TranslateService
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(login) {
        this.userService.deleteUserAmin(login).subscribe(response => {
            this.toastr.success(
                this.translate.instant('ebwebApp.organizationUnit.deleteSuccess'),
                this.translate.instant('ebwebApp.organizationUnit.message')
            );
            this.eventManager.broadcast({
                name: 'userListModification',
                content: 'Deleted a user'
            });
            this.activeModal.dismiss(true);
        });
    }
}
