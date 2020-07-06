// import { Component, OnDestroy, OnInit } from '@angular/core';
// import { ActivatedRoute, Router } from '@angular/router';
//
// import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
// import { JhiEventManager } from 'ng-jhipster';
//
// import { IPporder } from 'app/shared/model/pporder.model';
// import { PporderService } from './pporder.service';
//
// @Component({
//     selector: 'eb-pporder-delete-dialog',
//     templateUrl: './pporder-delete-dialog.component.html'
// })
// export class PporderDeleteDialogComponent {
//     pporder: IPporder;
//
//     constructor(private pporderService: PporderService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}
//
//     clear() {
//         this.activeModal.dismiss('cancel');
//     }
//
//     confirmDelete(id: number) {
//         this.pporderService.delete(id).subscribe(response => {
//             this.eventManager.broadcast({
//                 name: 'pporderListModification',
//                 content: 'Deleted an pporder'
//             });
//             this.activeModal.dismiss(true);
//         });
//     }
// }
//
// @Component({
//     selector: 'eb-pporder-delete-popup',
//     template: ''
// })
// export class PporderDeletePopupComponent implements OnInit, OnDestroy {
//     private ngbModalRef: NgbModalRef;
//
//     constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}
//
//     ngOnInit() {
//         this.activatedRoute.data.subscribe(({ pporder }) => {
//             setTimeout(() => {
//                 this.ngbModalRef = this.modalService.open(PporderDeleteDialogComponent as Component, {
//                     size: 'lg',
//                     backdrop: 'static'
//                 });
//                 this.ngbModalRef.componentInstance.pporder = pporder;
//                 this.ngbModalRef.result.then(
//                     result => {
//                         this.router.navigate([{ outlets: { popup: null } }], {
//                             replaceUrl: true,
//                             queryParamsHandling: 'merge'
//                         });
//                         this.ngbModalRef = null;
//                     },
//                     reason => {
//                         this.router.navigate([{ outlets: { popup: null } }], {
//                             replaceUrl: true,
//                             queryParamsHandling: 'merge'
//                         });
//                         this.ngbModalRef = null;
//                     }
//                 );
//             }, 0);
//         });
//     }
//
//     ngOnDestroy() {
//         this.ngbModalRef = null;
//     }
// }
