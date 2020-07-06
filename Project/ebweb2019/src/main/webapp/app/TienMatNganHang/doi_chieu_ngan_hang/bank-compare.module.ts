import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { bankCompareRoute } from 'app/TienMatNganHang/doi_chieu_ngan_hang/bank-compare.route';
import { EbwebSharedModule } from 'app/shared';
import { BankCompareComponent } from 'app/TienMatNganHang/doi_chieu_ngan_hang/bank-compare.component';
import { ViewBankCompareComponent } from 'app/TienMatNganHang/doi_chieu_ngan_hang/view-bank-compare.component';
import { UnBankCompareComponent } from 'app/TienMatNganHang/doi_chieu_ngan_hang/un-bank-compare.component';

const ENTITY_STATES = [...bankCompareRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [BankCompareComponent, ViewBankCompareComponent, UnBankCompareComponent],
    entryComponents: [BankCompareComponent, ViewBankCompareComponent, UnBankCompareComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebBankCompareModule {}
