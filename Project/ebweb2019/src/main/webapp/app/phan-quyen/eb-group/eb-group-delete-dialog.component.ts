import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { User, UserService } from 'app/core';
import { IEbGroup } from 'app/core/eb-group/eb-group.model';
import { EbGroupService } from 'app/phan-quyen/eb-group/eb-group.service';

@Component({
    selector: 'eb-group-delete-dialog',
    templateUrl: './eb-group-delete-dialog.component.html'
})
export class EbGroupDeleteDialogComponent {
    ebGroup: IEbGroup;

    constructor(private ebGroupService: EbGroupService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.ebGroupService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'ebGroupListModification',
                content: 'Deleted a ebGroup'
            });
            this.activeModal.dismiss(true);
        });
    }
}
