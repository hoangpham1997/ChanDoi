import { Component } from '@angular/core';
import { Subject } from 'rxjs/Subject';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
    selector: 'eb-confirm-leave',
    templateUrl: './confirm-leave.component.html'
})
export class ConfirmLeaveComponent {
    subject: Subject<boolean>;

    constructor(public bsModalRef: NgbActiveModal) {}

    action(value: boolean) {
        this.bsModalRef.close();
        this.subject.next(value);
        this.subject.complete();
    }
}
