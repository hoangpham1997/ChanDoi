import { Pipe } from '@angular/core';
import {
    DDSo_DocTienLe,
    DDSo_DonGia,
    DDSo_DonGiaNT,
    DDSo_NCachHangDVi,
    DDSo_NCachHangNghin,
    DDSo_NgoaiTe,
    DDSo_SoAm,
    DDSo_SoLuong,
    DDSo_TienVND,
    DDSo_TyGia,
    DDSo_TyLe,
    DDSo_TyLePBo,
    SO_NGUYEN
} from 'app/app.constants';

@Pipe({
    name: 'ebcurrency'
})
export class EbCurrencyPipe {
    transform(val: number, ...args: any[]) {
        const type = args[0];
        const systemOptions = args[1] && args[1].systemOption ? args[1].systemOption : null;
        if (!val) {
            return '0';
        }
        let optionsTemplate = {
            decimal: ',',
            precision: 0,
            thousands: '.',
            negative: '-'
        };
        if (systemOptions && type !== undefined && type !== null) {
            optionsTemplate = this.getOptionFromSys(systemOptions, this.getStringFromType(type));
            let final = val.toFixed(optionsTemplate.precision).replace('.', optionsTemplate.decimal);
            if (optionsTemplate.precision > 0) {
                const arr = final.split(optionsTemplate.decimal);
                let integerPart = arr[0];
                const decimalPart = arr[1];
                integerPart = integerPart.replace(/\B(?=(\d{3})+(?!\d))/g, optionsTemplate.thousands);
                final = integerPart + optionsTemplate.decimal + decimalPart;
            } else {
                final = final.replace(/\B(?=(\d{3})+(?!\d))/g, optionsTemplate.thousands);
            }
            if (val < 0 && optionsTemplate.negative !== '-') {
                final = final.replace('-', '(') + ')';
            }
            return final;
        }
        return '0';
    }

    getStringFromType(index: number) {
        const typeArray = [
            DDSo_DonGia,
            DDSo_DonGiaNT,
            DDSo_SoLuong,
            DDSo_TyGia,
            DDSo_TyLe,
            DDSo_TyLePBo,
            DDSo_TienVND,
            DDSo_NgoaiTe,
            SO_NGUYEN
        ];
        if (index < 0 || index > typeArray.length) {
            return typeArray[0];
        }
        return typeArray[index - 1];
    }

    getOptionFromSys(systemOption, type) {
        const option = {
            decimal: ',',
            precision: 0,
            thousands: '.',
            negative: '-'
        };

        for (const item of systemOption) {
            switch (item.code) {
                case DDSo_NCachHangNghin:
                    option.thousands = item.data;
                    break;
                case DDSo_NCachHangDVi:
                    option.decimal = item.data;
                    break;
                case DDSo_DocTienLe:
                    break;
                case DDSo_SoAm:
                    if (item.data === '0') {
                        option.negative = '(';
                    }
                    break;
                case SO_NGUYEN:
                    break;
                case type:
                    option.precision = parseInt(item.data, 10);
                    break;
            }
        }
        return option;
    }
}
