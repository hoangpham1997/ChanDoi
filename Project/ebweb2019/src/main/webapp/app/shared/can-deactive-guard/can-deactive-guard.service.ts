import { ActivatedRouteSnapshot, CanDeactivate, RouterStateSnapshot } from '@angular/router';
import { Injectable } from '@angular/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Observable, Subject } from 'rxjs';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ConfirmLeaveComponent } from 'app/shared/can-deactive-guard/confirm-leave.component';

@Injectable()
export class CanDeactiveGuardService implements CanDeactivate<BaseComponent> {
    constructor(private modalService: NgbModal) {}

    canDeactivate(component: BaseComponent) {
        if (!component.canDeactive()) {
            const subject = new Subject<boolean>();
            const modal = this.modalService.open(ConfirmLeaveComponent, { backdrop: 'static' });
            modal.componentInstance.subject = subject;

            return subject.asObservable();
        }
        return true;
    }
}
