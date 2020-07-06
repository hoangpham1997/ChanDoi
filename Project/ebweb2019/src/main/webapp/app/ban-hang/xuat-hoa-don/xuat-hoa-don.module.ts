import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { EbwebSharedModule } from 'app/shared';
import { NgSelectModule } from '@ng-select/ng-select';
import { xuatHoaDon, xuatHoaDonPopupRoute } from 'app/ban-hang/xuat-hoa-don/xuat-hoa-don.route';
import { XuatHoaDonComponent } from 'app/ban-hang/xuat-hoa-don/xuat-hoa-don.component';
import { XuatHoaDonUpdateComponent } from 'app/ban-hang/xuat-hoa-don/xuat-hoa-don-update.component';
import {
    XuatHoaDonDeleteDialogComponent,
    XuatHoaDonDeletePopupComponent
} from 'app/ban-hang/xuat-hoa-don/xuat-hoa-don-delete-dialog.component';
import { EbContextMenuModule } from 'app/shared/context-menu/contex-menu.module';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';

const ENTITY_STATES = [...xuatHoaDon, ...xuatHoaDonPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, ReactiveFormsModule, RouterModule.forChild(ENTITY_STATES), NgSelectModule, EbContextMenuModule],
    declarations: [XuatHoaDonComponent, XuatHoaDonUpdateComponent, XuatHoaDonDeletePopupComponent, XuatHoaDonDeleteDialogComponent],
    entryComponents: [XuatHoaDonComponent, XuatHoaDonUpdateComponent, XuatHoaDonDeletePopupComponent, XuatHoaDonDeleteDialogComponent],
    providers: [UtilsService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebXuatHoaDonModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
