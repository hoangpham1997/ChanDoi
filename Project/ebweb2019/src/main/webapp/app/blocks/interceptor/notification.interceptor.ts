import { JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { HttpInterceptor, HttpRequest, HttpResponse, HttpHandler, HttpEvent } from '@angular/common/http';
import { Injector } from '@angular/core';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';

export class NotificationInterceptor implements HttpInterceptor {
    private alertService: JhiAlertService;
    private eventManager: JhiEventManager;
    private toastr: ToastrService;
    private translate: TranslateService;

    // tslint:disable-next-line: no-unused-variable
    constructor(private injector: Injector) {
        setTimeout(() => (this.alertService = injector.get(JhiAlertService)));
        setTimeout(() => (this.eventManager = injector.get(JhiEventManager)));
        setTimeout(() => (this.toastr = injector.get(ToastrService)));
        setTimeout(() => (this.translate = injector.get(TranslateService)));
    }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        return next.handle(request).pipe(
            tap(
                (event: HttpEvent<any>) => {
                    if (event instanceof HttpResponse) {
                        const arr = event.headers.keys();
                        let alert = null;
                        let alertParams = null;
                        let updateData = null;
                        arr.forEach(entry => {
                            if (entry.toLowerCase().endsWith('app-alert')) {
                                alert = event.headers.get(entry);
                            } else if (entry.toLowerCase().endsWith('app-params')) {
                                alertParams = event.headers.get(entry);
                            } else if (entry.toLowerCase().endsWith('app-update-data')) {
                                updateData = event.headers.get(entry);
                            }
                        });
                        if (alert) {
                            if (typeof alert === 'string') {
                                if (this.alertService) {
                                    this.alertService.success(alert, { param: alertParams }, null);
                                }
                            }
                        }
                        if (updateData) {
                            this.eventManager.broadcast({
                                name: 'update-data',
                                content: decodeURIComponent(escape(window.atob(updateData)))
                            });
                        }
                    }
                },
                (err: any) => {
                    if (err && err.error && err.error.errorKey === 'noVoucherLimited') {
                        this.toastr.error(this.translate.instant(`ebwebApp.mBTellerPaper.error.${err.error.errorKey}`));
                    }
                }
            )
        );
    }
}
