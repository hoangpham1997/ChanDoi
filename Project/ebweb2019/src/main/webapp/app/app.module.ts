import './vendor.ts';

import { Injector, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { NgbDatepickerConfig } from '@ng-bootstrap/ng-bootstrap';
import { LocalStorageService, Ng2Webstorage, SessionStorageService } from 'ngx-webstorage';
import { JhiEventManager } from 'ng-jhipster';

import { AuthInterceptor } from './blocks/interceptor/auth.interceptor';
import { AuthExpiredInterceptor } from './blocks/interceptor/auth-expired.interceptor';
import { ErrorHandlerInterceptor } from './blocks/interceptor/errorhandler.interceptor';
import { NotificationInterceptor } from './blocks/interceptor/notification.interceptor';
import { EbwebSharedModule } from 'app/shared';
import { EbwebCoreModule } from 'app/core';
import { EbwebAppRoutingModule } from './app-routing.module';
import { EbwebHomeModule } from './home/home.module';
import { EbwebAccountModule } from './account/account.module';
import * as moment from 'moment';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { ActiveMenuDirective, EbMainComponent, ErrorComponent, FooterComponent, LoginComponent, NavbarComponent } from './layouts';
import { SidebarComponent } from 'app/layouts/sidebar/sidebar.component';
import { EbwebContextMenuModule } from 'app/contextmenu/contexmenu.module';
import { EbContextMenuModule } from 'app/shared/context-menu/contex-menu.module';
import { NgxMaskModule } from 'ngx-mask';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { LoadingBarHttpClientModule } from '@ngx-loading-bar/http-client';
import { AdminLoginComponent } from 'app/admin/login/login.component';
import { ActiveMenuAdminDirective } from 'app/admin/navbar/active-menu.directive';
import { EbVirtualScrollerModule } from 'app/virtual-scroller/virtual-scroller';
import { EbwebQuyTrinhModule } from 'app/quy-trinh/quytrinh.module';

@NgModule({
    imports: [
        BrowserModule,
        BrowserAnimationsModule, // required animations module
        EbwebAppRoutingModule,
        Ng2Webstorage.forRoot({ prefix: '', separator: '' }),
        NgxMaskModule.forRoot(),
        EbwebSharedModule,
        EbwebCoreModule,
        EbwebHomeModule,
        EbwebAccountModule,
        EbwebQuyTrinhModule,
        EbwebContextMenuModule,
        EbContextMenuModule,
        LoadingBarHttpClientModule,
        EbVirtualScrollerModule
        // jhipster-needle-angular-add-module JHipster will add new module here
    ],
    declarations: [
        EbMainComponent,
        SidebarComponent,
        NavbarComponent,
        ErrorComponent,
        ActiveMenuDirective,
        ActiveMenuAdminDirective,
        FooterComponent,
        LoginComponent,
        AdminLoginComponent
    ],
    providers: [
        {
            provide: HTTP_INTERCEPTORS,
            useClass: AuthInterceptor,
            multi: true,
            deps: [LocalStorageService, SessionStorageService]
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: AuthExpiredInterceptor,
            multi: true,
            deps: [Injector]
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: ErrorHandlerInterceptor,
            multi: true,
            deps: [JhiEventManager]
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: NotificationInterceptor,
            multi: true,
            deps: [Injector]
        }
    ],
    bootstrap: [EbMainComponent]
})
export class EbwebAppModule {
    constructor(private dpConfig: NgbDatepickerConfig) {
        this.dpConfig.minDate = { year: moment().year() - 100, month: 1, day: 1 };
    }
}
