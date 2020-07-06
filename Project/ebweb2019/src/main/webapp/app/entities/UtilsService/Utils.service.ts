import { EventEmitter, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { createRequestOption, DATE_FORMAT } from 'app/shared';
import {
    CategoryName,
    DDSo_DonGia,
    DDSo_DonGiaNT,
    DDSo_NgoaiTe,
    DDSo_SoLuong,
    DDSo_TienVND,
    DDSo_TyGia,
    DDSo_TyLe,
    DDSo_TyLePBo,
    NGAY_HACH_TOAN,
    SERVER_API_URL,
    SO_NGUYEN,
    TangSoChungTu,
    TCKHAC_MauCTuChuaGS
} from 'app/app.constants';
import { IAutoPrinciple } from 'app/shared/model/auto-principle.model';
import { IBank } from 'app/shared/model/bank.model';
import { IAccountingObject, VoucherRefCatalogDTO } from 'app/shared/model/accounting-object.model';
import { ICurrency } from 'app/shared/model/currency.model';
import { IBankAccountDetails } from 'app/shared/model/bank-account-details.model';
import { IStatisticsCode } from 'app/shared/model/statistics-code.model';
import { ICostSet } from 'app/shared/model/cost-set.model';
import { IExpenseItem } from 'app/shared/model/expense-item.model';
import { IContractState } from 'app/shared/model/contract-state.model';
import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';
import { IAccountList } from 'app/shared/model/account-list.model';
import { IAccountDefault } from 'app/shared/model/account-default.model';
import * as moment from 'moment';
import { Moment } from 'moment';
import { map } from 'rxjs/operators';
import { DatePipe } from '@angular/common';
import { TranslateService } from '@ngx-translate/core';
import { ToastrService } from 'ngx-toastr';
import { IEMContract } from 'app/shared/model/em-contract.model';
import { IMaterialGoods } from 'app/shared/model/material-goods.model';
import { IBudgetItem } from 'app/shared/model/budget-item.model';
import * as cloneDeep from 'lodash/cloneDeep';
import { UpdateDataMessages } from 'app/muahang/mua-dich-vu/mua-dich-vu.constant';
import { ViewVoucherService } from 'app/shared/modal/ref/view-voucher.service';
import { IMCReceipt } from 'app/shared/model/mc-receipt.model';
import { MaterialGoodsService } from 'app/danhmuc/material-goods';
import { JhiEventManager } from 'ng-jhipster';

type EntityResponseType = HttpResponse<object>;
type EntityArrayResponseType = HttpResponse<object[]>;

@Injectable({
    providedIn: 'root'
})
/*Create by Hautv*/
export class UtilsService {
    checkEvent = new EventEmitter();
    isShowPopup: boolean;

    /*
    * @author namnh
    * Lấy tiêu đề và dữ liệu lên combobox
    * */
    checkSLT: boolean;
    listColumnsAccountDefaults: string[] = ['accountNumber', 'accountName'];
    listColumnsAccountDefaults1: string[] = ['accountNumber'];
    listHeaderColumnsAccountDefaults: string[] = ['Tài khoản', 'Tên tài khoản'];
    listColumnInvoiceType: string[] = ['invoiceTypeCode', 'invoiceTypeName'];
    listHeaderColumnInvoiceType: string[] = ['Mã mẫu số hóa đơn', 'Tên mẫu số hóa đơn'];
    listColumnsTimeLine: string[] = ['display'];
    listHeaderColumnsTimeLine: string[] = ['Thời gian'];
    listHeaderColumnsAccountingObject: string[] = ['Mã đối tượng', 'Tên đối tượng', 'Địa chỉ'];
    listColumnsAccountingObjectsSearch: string[] = ['accountingObjectCode', 'accountingObjectName'];
    listHeaderColumnsAccountingObjectsSearch: string[] = ['Mã đối tượng', 'Tên đối tượng'];
    danhSachCotMaHang: string[] = ['materialGoodsCode', 'materialGoodsName', 'materialGoodsInStock'];
    danhSachTieuDeMaHang: string[] = ['Mã hàng', 'Tên hàng', 'Số lượng tồn'];
    listColumnsAccountingObjects: string[] = ['accountingObjectCode', 'accountingObjectName', 'accountingObjectAddress'];
    listColumnsEmployees: string[] = ['accountingObjectCode', 'accountingObjectName'];
    listHeaderColumnsEmployees: string[] = ['Mã nhân viên', 'Tên nhân viên'];
    listColumnsBank: string[] = ['bankAccount', 'bankName'];
    listColumnsBanks: string[] = ['bankCode', 'bankName'];
    listHeaderColumnsBank: string[] = ['Số tài khoản ngân hàng', 'Tên ngân hàng'];
    listHeaderColumnsBanks: string[] = ['Mã ngân hàng', 'Tên ngân hàng'];
    listColumnsCurrency: string[] = ['currencyCode', 'currencyName'];
    listHeaderColumnsCurrency: string[] = ['Mã loại tiền', 'Tên loại tiền'];
    listColumnsAccountList: string[] = ['accountNumber', 'accountName'];
    listHeaderColumnsAccountList: string[] = ['Tài khoản', 'Tên tài khoản'];
    listColumnsExpenseItem: string[] = ['expenseItemCode', 'expenseItemName'];
    listColumnsExpenseItemCode: string[] = ['value', 'name'];
    listHeaderColumnsExpenseItem: string[] = ['Mã khoản mục CP', 'Tên khoản mục CP'];
    listColumnsGroupExpenseItemCode: string[] = ['name'];
    listHeaderColumnsGroupExpenseItem: string[] = ['Loại chi phí'];
    listColumnsOrganizationUnit: string[] = ['organizationUnitCode', 'organizationUnitName'];
    listColumnsUnit: string[] = ['unitName'];
    listHeaderColumnsUnit: string[] = ['Đơn vị'];
    listHeaderColumnsOrganizationUnit: string[] = ['Mã phòng ban', 'Tên phòng ban'];
    listHeadercolumkhoanmucchiphi: string[] = ['mã khoan muc chi phí', 'tên khoan mục chi phí'];
    listColumnsCostSet: string[] = ['costSetCode', 'costSetName'];
    listHeaderColumnsCostSet: string[] = ['Mã ĐT THCP', 'Tên ĐT THCP'];
    listColumnsContractState: string[] = ['id', 'contractStateName'];
    listHeaderColumnsContractState: string[] = ['ID hợp đồng', 'Tên hợp đồng'];
    listColumnsStatisticCode: string[] = ['statisticsCode', 'statisticsCodeName'];
    listHeaderColumnsStatisticCode: string[] = ['Mã thống kê', 'Tên mã thống kê'];
    listColumnsAutoPrinciple: string[] = ['autoPrincipleName', 'debitAccount', 'creditAccount'];
    listHeaderColumnsAutoPrinciple: string[] = ['Tên định khoản', 'Tài khoản nợ', 'Tài khoản có'];
    listColumnsFixedAssetCategory: string[] = ['fixedAssetCategoryCode', 'fixedAssetCategoryName'];
    listHeaderColumnsFixedAssetCategory: string[] = ['Mã loại TSCĐ', 'Tên loại TSCĐ'];
    listColumnsWarranty: string[] = ['warrantyTime', 'warrantyName'];
    listHeaderColumnsWarranty: string[] = ['Thời gian bảo hành', 'Tên thời hạn bảo hành'];
    listColumnsBankAccountDetails: string[] = ['bankAccount', 'bankName'];
    listHeaderColumnsBankAccountDetails: string[] = ['Số tài khoản ngân hàng', 'Tên ngân hàng'];
    listColumnsCreditCard: string[] = ['creditCardNumber', 'creditCardType'];
    listHeaderColumnsCreditCard: string[] = ['Số thẻ', 'Loại thẻ'];
    listColumnsAccountingObjectBankAccount: string[] = ['bankAccount', 'bankName'];
    listColumnsAccountingObjectBankAccount1: string[] = ['bankAccount', 'bankBranchName'];
    listHeaderColumnsAccountingObjectBankAccount: string[] = ['Số thẻ', 'Loại thẻ'];
    listColumnsEMContract: string[] = ['noMBook', 'description'];
    listColumnsEMContract1: string[] = ['noFBook', 'description'];
    listHeaderColumnsEMContract: string[] = ['Số hợp đồng', 'Trích yếu'];
    listColumnsEMContractMBook: string[] = ['noMBook', 'description'];
    listHeaderColumnsEMContractMBook: string[] = ['Số hợp đồng', 'Trích yếu'];
    listColumnsMonth: string[] = ['month'];
    listHeaderColumnsMonth: string[] = ['Tháng'];
    listColumnsEMContractFBook: string[] = ['noFBook', 'description'];
    listHeaderColumnsEMContractFBook: string[] = ['Số hợp đồng', 'Trích yếu'];
    listColumnsMaterialGoods: string[] = ['materialGoodsCode', 'materialGoodsName', 'materialGoodsInStock'];
    listColumnsMaterialGoodsPPService: string[] = ['materialGoodsCode', 'materialGoodsName'];
    listHeaderColumnsMaterialGoods: string[] = ['Mã hàng', 'Tên hàng', 'Số lượng tồn'];
    HeaderColumnsMaterialGoods: string[] = ['Mã hàng', 'Tên hàng'];
    listColumnsRepository: string[] = ['repositoryCode', 'repositoryName'];
    listHeaderColumnsRepository: string[] = ['Mã kho', 'Tên kho'];
    // listColumnsEMContract: string[] = ['noMBook'];
    // listHeaderColumnsEMContract: string[] = ['Số hợp đồng'];
    listColumnsBudgetItem: string[] = ['budgetItemCode', 'budgetItemName'];
    listHeaderColumnsBudgetItem: string[] = ['Mã mục thu/chi', 'Tên mục thu/chi'];
    listColumnsType: string[] = ['typeName'];
    listHeaderColumnsType: string[] = ['Loại chứng từ'];
    listColumnsTypeGroup: string[] = ['typeGroupName'];
    listHeaderColumnsTypeGroup: string[] = ['Loại chứng từ'];
    listColumnsTypeGroupForResetNo: string[] = ['typeGroupName', 'prefix', 'currentValue'];
    listHeaderColumnsTypeGroupForResetNo: string[] = ['Loại chứng từ', 'Mã chứng từ', 'Giá trị hiện tại'];
    listColumnsCPPeriod: string[] = ['name'];
    listHeaderColumnsCPPeriod: string[] = ['Kỳ tính giá thành'];
    listHeaderColumnsStatus: string[] = ['Trạng thái'];
    listColumnsChoseFuntion: string[] = ['name'];
    listHeaderstatusRecordHeader: string[] = ['Trạng thái'];
    listHeaderColumnsChoseFuntion: string[] = ['Cách xử lý'];
    listColumnsAccountGroup: string[] = ['id', 'accountGroupName'];
    listHeaderColumnsAccountGroup: string[] = ['Nhóm tài khoản', 'Tên nhóm tài khoản'];
    listColumnsMethods: string[] = ['transportMethodCode', 'transportMethodName'];
    listHeaderColumnsMethods: string[] = ['Mã phương thức', 'Tên phương thức'];

    // listColumnsMaterialGoods: string[] = ['materialGoodsCode', 'materialGoodsName', 'materialGoodsInStock'];
    listHeaderMaterialGoods: string[] = ['Mã hàng', 'Tên hàng'];
    listHeaderMaterialGoodsForPPservices: string[] = ['Mã dịch vụ', 'Tên dịch vụ', 'Số lượng tồn'];
    listColumnsReason: string[] = ['autoPrincipleName'];
    listHeaderColumnsReason: string[] = ['Diễn giải'];
    // listHeaderMaterialGoodsForPPservices: string[] = ['Mã dịch vụ', 'Tên dịch vụ'];
    ColumnsMaterialGoods: string[] = ['materialGoodsCode', 'materialGoodsName'];
    // listHeaderMaterialGoods: string[] = ['Mã hàng', 'Tên hàng'];

    // listHeaderMaterialGoodsForPPservices: string[] = ['Mã dịch vụ', 'Tên dịch vụ'];
    listHeaderColumnsGoodsServicePurchase: string[] = ['Mã HDDV', 'Tên HDDV'];
    listColumnsGoodsServicePurchase: string[] = ['goodsServicePurchaseCode', 'goodsServicePurchaseName'];
    listHeaderColumnsCareerGroups: string[] = ['Mã nhóm', 'Tên nhóm'];
    listColumnsCareerGroups: string[] = ['careerGroupCode', 'careerGroupName'];
    listHeaderRepository: string[] = ['Mã kho', 'Tên kho'];
    listColumnsPaymentClause: string[] = ['paymentClauseCode', 'paymentClauseName'];
    listHeaderPaymentClause: string[] = ['Mã điều khoản TT', 'Tên điều khoản TT'];
    listColumnsPrepaidExpenseCode: string[] = ['species', 'code', 'name'];
    listHeaderPrepaidExpenseCode: string[] = ['Loại ĐT', 'Mã ĐT', 'Tên ĐT'];
    listColumnsPrepaidExpense: string[] = ['prepaidExpenseCode', 'prepaidExpenseName'];
    listHeaderPrepaidExpense: string[] = ['Mã chi phí trả trước', 'Tên chi phí trả trước'];
    listColumnsLotNo: string[] = ['lotNo', 'expiryDate', 'totalQuantityBalance'];
    listHeaderColumnsLotNo: string[] = ['Số lô ', 'Hạn dùng', 'Số lượng tồn'];
    listColumnsCreateFromScene: string[] = ['name'];
    listColumnsmaterialGoodsCategory = ['materialGoodsCategoryCode', 'materialGoodsCategoryName'];
    listHeaderColumnsmaterialGoodsCategor = ['Mã loại', 'Tên loại'];

    listColumnsEbPackage = ['packageCode', 'packageName'];
    listHeaderColumnsEbPackage = ['Mã gói', 'Tên gói'];

    listColumnsCustomer = ['accountingObjectGroupCode', 'accountingObjectGroupName'];
    listHeaderColumnsCustomer = ['Mã khách hàng', 'Tên khách hàng'];
    listColumnsTools = ['toolsCode', 'toolsName'];
    listHeaderColumnsTools = ['Mã CCDC', 'Tên CCDC'];
    listColumnsFixedAssets = ['fixedAssetCode', 'fixedAssetName'];
    listHeaderColumnsFixedAssets = ['Mã TSCD', 'Tên TSCD'];

    listColumnsAttributionCriteriaList = ['name'];
    listHeaderColumnsAttributionCriteriaList = ['Tiêu thức phân bổ'];

    listColumnsMethodList = ['name'];
    listHeaderColumnsMethodList = ['Phương pháp'];

    listColumnsToolsConvert = ['toolsCode', 'toolsName', 'quantityRest'];
    listHeaderColumnsToolsConvert = ['Mã CCDC', 'Tên CCDC', 'Số lượng còn'];
    listColumnsOrganizationUnitName = ['organizationUnitName'];
    listHeaderColumnsOrganizationUnitName = ['Tên công ty'];
    // end
    banks: IBank[];
    employees: IAccountingObject[];
    currency: ICurrency[];
    bankAccountDetails: IBankAccountDetails[];
    statisticsCode: IStatisticsCode[];
    costSets: ICostSet[];
    expenseItems: IExpenseItem[];
    contractStates: IContractState[];
    accountList: IAccountList[];
    autoPrinciples: IAutoPrinciple[];
    organizationUnits: IOrganizationUnit[];
    accountDefaults: IAccountDefault[];
    accountingObjects: IAccountingObject[];
    dtBeginTemp: any;
    dtEndTemp: any;
    dtBeginDate: any;
    dtEndDate: any;
    objTimeLine: { dtBeginDate?: string; dtEndDate?: string };
    listTypeByDebitAccount: number[] = [110, 120, 130, 140, 150, 170];
    listTypeByCreditAccount: number[] = [700];
    listTimeLine: any[];
    DOI_TUONG: any = CategoryName.DOI_TUONG;
    KHACH_HANG: any = CategoryName.KHACH_HANG;
    NHA_CUNG_CAP: any = CategoryName.NHA_CUNG_CAP;
    NHAN_VIEN: any = CategoryName.NHAN_VIEN;
    TAI_KHOAN_NGAN_HANG: any = CategoryName.TAI_KHOAN_NGAN_HANG;
    NGAN_HANG: any = CategoryName.NGAN_HANG;
    MA_THONG_KE: any = CategoryName.MA_THONG_KE;
    HOP_DONG: any = CategoryName.HOP_DONG;
    MUC_THU_CHI: any = CategoryName.MUC_THU_CHI;
    VAT_TU_HANG_HOA: any = CategoryName.VAT_TU_HANG_HOA;
    VAT_TU_HANG_HOA_PP_SERVICE: any = CategoryName.VAT_TU_HANG_HOA_PP_SERVICE;
    THE_TIN_DUNG: any = CategoryName.THE_TIN_DUNG;
    LOAI_TIEN: any = CategoryName.LOAI_TIEN;
    DON_VI_TINH: any = CategoryName.DON_VI_TINH;
    KHOAN_MUC_CHI_PHI: any = CategoryName.KHOAN_MUC_CHI_PHI;
    KHO: any = CategoryName.KHO;
    LOAI: any = CategoryName.LOAI_VAT_TU_HANG_HOA;
    DOI_TUONG_TAP_HOP_CHI_PHI: any = CategoryName.DOI_TUONG_TAP_HOP_CHI_PHI;
    PHONG_BAN: any = CategoryName.PHONG_BAN;
    PHUONG_THUC_VAN_CHUYEN: any = CategoryName.PHUONG_THUC_VAN_CHUYEN;

    // list time line

    constructor(
        private http: HttpClient,
        private translate: TranslateService,
        private datepipe: DatePipe,
        private toastr: ToastrService,
        private viewVoucherService: ViewVoucherService
    ) {}

    setShowPopup(data) {
        this.isShowPopup = data;
        this.checkEvent.emit(data);
    }
    /*
      Author Hautv
    * Hàm tính tính toán tiền , thuế
    * */
    public calcular(
        objcet1: object,
        typeID: number,
        name: string,
        objectParent: object,
        objectDT1: object[],
        objectDT2: object[],
        account?,
        typeOriginal?,
        exchangeRate?
    ) {
        if (!exchangeRate) {
            exchangeRate = objectParent['exchangeRate'];
        }
        if (name !== 'vATRate' && name !== 'creditAccount' && name !== 'debitAccount' && (objcet1[name] === '' || objcet1[name] === null)) {
            objcet1[name] = 0;
        }
        if (name === 'pretaxAmount') {
            if (objcet1['pretaxAmount'] && objectParent['exchangeRate']) {
                if (account) {
                    objcet1['pretaxAmountOriginal'] = this.round(
                        parseFloat(objcet1['pretaxAmount']) / parseFloat(exchangeRate.toString()),
                        account.systemOption,
                        typeOriginal
                    );
                } else {
                    objcet1['pretaxAmountOriginal'] = parseFloat(objcet1['pretaxAmount']) / parseFloat(exchangeRate.toString());
                }
            }
        }
        if (name === 'amount' && this.listTypeByDebitAccount.find(n => n.valueOf() === typeID)) {
            objectParent['totalAmount'] = 0;
            objectParent['totalAmountOriginal'] = 0;
            for (let i = 0; i < objectDT1.length; i++) {
                if (account) {
                    objectParent['totalAmount'] = this.round(
                        parseFloat(objectParent['totalAmount']) + parseFloat(objectDT1[i]['amount']),
                        account.systemOption,
                        7
                    );
                    objectParent['totalAmountOriginal'] = this.round(
                        parseFloat(objectParent['totalAmountOriginal']) + parseFloat(objectDT1[i]['amountOriginal']),
                        account.systemOption,
                        typeOriginal
                    );
                } else {
                    objectParent['totalAmount'] = parseFloat(objectParent['totalAmount']) + parseFloat(objectDT1[i]['amount']);
                    objectParent['totalAmountOriginal'] =
                        parseFloat(objectParent['totalAmountOriginal']) + parseFloat(objectDT1[i]['amountOriginal']);
                }
            }
        } else if (name === 'vATRate' || name === 'vATAmount') {
            if (objcet1['vATRate'] === 0 || objcet1['vATRate'] === 3 || objcet1['vATRate'] === 4) {
                objcet1['pretaxAmount'] = 0;
                objcet1['pretaxAmountOriginal'] = 0;
            } else {
                if (account) {
                    if (objcet1['vATRate'] === 1) {
                        /* 5% */
                        objcet1['pretaxAmount'] = this.round(parseFloat(objcet1['vATAmount']) / 5 * 100, account.systemOption, 7);
                        if (objcet1['pretaxAmount'] && objectParent['exchangeRate']) {
                            objcet1['pretaxAmountOriginal'] = this.round(
                                parseFloat(objcet1['pretaxAmount']) / parseFloat(exchangeRate.toString()),
                                account.systemOption,
                                typeOriginal
                            );
                        }
                    } else if (objcet1['vATRate'] === 2) {
                        /* 10% */
                        objcet1['pretaxAmount'] = this.round(parseFloat(objcet1['vATAmount']) / 10 * 100, account.systemOption, 7);
                        if (objcet1['pretaxAmount'] && objectParent['exchangeRate']) {
                            objcet1['pretaxAmountOriginal'] = this.round(
                                parseFloat(objcet1['pretaxAmount']) / parseFloat(exchangeRate.toString()),
                                account.systemOption,
                                typeOriginal
                            );
                        }
                    } else {
                        objcet1['pretaxAmount'] = 0;
                        objcet1['pretaxAmountOriginal'] = 0;
                    }
                } else {
                    if (objcet1['vATRate'] === 1) {
                        /* 5% */
                        objcet1['pretaxAmount'] = parseFloat(objcet1['vATAmount']) / 5 * 100;
                        if (objcet1['pretaxAmount'] && objectParent['exchangeRate']) {
                            objcet1['pretaxAmountOriginal'] = parseFloat(objcet1['pretaxAmount']) / parseFloat(exchangeRate.toString());
                        }
                    } else if (objcet1['vATRate'] === 2) {
                        /* 10% */
                        objcet1['pretaxAmount'] = parseFloat(objcet1['vATAmount']) / 10 * 100;
                        if (objcet1['pretaxAmount'] && objectParent['exchangeRate']) {
                            objcet1['pretaxAmountOriginal'] = parseFloat(objcet1['pretaxAmount']) / parseFloat(exchangeRate.toString());
                        }
                    } else {
                        objcet1['pretaxAmount'] = 0;
                        objcet1['pretaxAmountOriginal'] = 0;
                    }
                }
            }
        } else {
            if (!account) {
                /*Tính tiền quy đổi*/
                objcet1['amount'] = parseFloat(objcet1['amountOriginal']) * parseFloat(exchangeRate.toString());
                // region Các TH tính thuế có phát sinh TK Nợ và TK có
                if (objcet1.hasOwnProperty('debitAccount') || objcet1.hasOwnProperty('creditAccount')) {
                    let totalVat133 = 0;
                    let totalVat133Original = 0;
                    let totalVat133And3331 = 0;
                    let totalVat133And3331Origianl = 0;
                    let totalVat3331 = 0;
                    let totalVat3331Original = 0;
                    const totalVat = 0;
                    const totalVatOriginal = 0;
                    let isVat133 = false;
                    let isVat3331 = false;
                    let isVat133And3331 = false;
                    const isVat = false;
                    let vat133Account = '';
                    let vat3331Account = '';
                    const vat133And3331Account = '';
                    const vatAccount = '';
                    // trường hợp tính thuế GOtherVoucher
                    // edit by namnh
                    if (this.listTypeByCreditAccount.find(n => n.valueOf() === typeID)) {
                        if (
                            (objcet1['creditAccount'] === null || objcet1['creditAccount'] === undefined
                                ? ''
                                : <string>objcet1['creditAccount']
                            ).startsWith('3331', 0) &&
                            (objcet1['debitAccount'] === null || objcet1['debitAccount'] === undefined
                                ? ''
                                : <string>objcet1['debitAccount']
                            ).startsWith('133', 0)
                        ) {
                            /*Tính tổng tiền có tài khoản bắt đầu 3331 và 133*/
                            for (let i = 0; i < objectDT1.length; i++) {
                                const t = <string>objectDT1[i]['creditAccount'];
                                const t2 = <string>objectDT1[i]['debitAccount'];
                                if (t && t2) {
                                    if (
                                        (<string>objectDT1[i]['creditAccount'] === null ? '' : <string>objectDT1[i]['creditAccount']) ===
                                            <string>objcet1['creditAccount'] &&
                                        (<string>objectDT1[i]['debitAccount'] === null ? '' : <string>objectDT1[i]['debitAccount']) ===
                                            <string>objcet1['debitAccount']
                                    ) {
                                        totalVat133And3331 =
                                            parseFloat(totalVat133And3331.toString()) + parseFloat(objectDT1[i]['amount'].toString());
                                        totalVat133And3331Origianl =
                                            parseFloat(totalVat133And3331Origianl.toString()) +
                                            parseFloat(objectDT1[i]['amountOriginal'].toString());
                                    }
                                }
                            }
                            isVat133And3331 = true;
                            // if (vat133And3331Account === '' || vat133And3331Account === null) {
                            //     vat133And3331Account = objcet1['creditAccount'];
                            // }
                        } else if (
                            (objcet1['creditAccount'] === null || objcet1['creditAccount'] === undefined
                                ? ''
                                : <string>objcet1['creditAccount']
                            ).startsWith('3331', 0)
                        ) {
                            /*Tính tổng tiền có tài khoản bắt đầu 3331*/
                            for (let i = 0; i < objectDT1.length; i++) {
                                const t = <string>objectDT1[i]['creditAccount'];
                                const t2 = <string>objectDT1[i]['debitAccount'];
                                let check133And3331debitAccount = false;
                                if (t2) {
                                    check133And3331debitAccount = t2.startsWith('133', 0);
                                }
                                if (t !== undefined && !check133And3331debitAccount) {
                                    if (
                                        (<string>objectDT1[i]['creditAccount'] === null ? '' : <string>objectDT1[i]['creditAccount']) ===
                                        <string>objcet1['creditAccount']
                                    ) {
                                        totalVat3331 = parseFloat(totalVat3331.toString()) + parseFloat(objectDT1[i]['amount'].toString());
                                        totalVat3331Original =
                                            parseFloat(totalVat3331Original.toString()) +
                                            parseFloat(objectDT1[i]['amountOriginal'].toString());
                                    }
                                }
                            }
                            isVat3331 = true;
                            if (vat3331Account === '' || vat3331Account === null) {
                                vat3331Account = objcet1['creditAccount'];
                            }
                        } else if (
                            (objcet1['debitAccount'] === null || objcet1['debitAccount'] === undefined
                                ? ''
                                : <string>objcet1['debitAccount']
                            ).startsWith('133', 0)
                        ) {
                            /*Tính tổng tiền có tài khoản bắt đầu 133*/
                            for (let i = 0; i < objectDT1.length; i++) {
                                const t = <string>objectDT1[i]['debitAccount'];
                                let check133And3331creditAccount = false;
                                const t2 = <string>objectDT1[i]['creditAccount'];
                                if (t2) {
                                    check133And3331creditAccount = t2.startsWith('3331', 0);
                                }
                                if (t !== undefined && !check133And3331creditAccount) {
                                    if (
                                        (<string>objectDT1[i]['debitAccount'] === null ? '' : <string>objectDT1[i]['debitAccount']) ===
                                        <string>objcet1['debitAccount']
                                    ) {
                                        totalVat133 = parseFloat(totalVat133.toString()) + parseFloat(objectDT1[i]['amount'].toString());
                                        totalVat133Original =
                                            parseFloat(totalVat133Original.toString()) +
                                            parseFloat(objectDT1[i]['amountOriginal'].toString());
                                    }
                                }
                            }
                            isVat133 = true;
                            if (vat133Account === '' || vat133Account === null) {
                                vat133Account = objcet1['debitAccount'];
                            }
                        }
                    } else if (this.listTypeByDebitAccount.find(n => n.valueOf() === typeID)) {
                        if (
                            (objcet1['debitAccount'] === null || objcet1['debitAccount'] === undefined
                                ? ''
                                : <string>objcet1['debitAccount']
                            ).startsWith('133', 0)
                        ) {
                            /*Tính tổng tiền có tài khoản bắt đầu 133*/
                            for (let i = 0; i < objectDT1.length; i++) {
                                const t = <string>objectDT1[i]['debitAccount'];
                                if (t !== undefined) {
                                    if (
                                        (<string>objectDT1[i]['debitAccount'] === null ? '' : <string>objectDT1[i]['debitAccount']) ===
                                        <string>objcet1['debitAccount']
                                    ) {
                                        totalVat133 = parseFloat(totalVat133.toString()) + parseFloat(objectDT1[i]['amount']);
                                        totalVat133Original =
                                            parseFloat(totalVat133Original.toString()) +
                                            parseFloat(objectDT1[i]['amountOriginal'].toString());
                                    }
                                }
                            }
                            isVat133 = true;
                            if (vat133Account === '' || vat133Account === null) {
                                vat133Account = objcet1['debitAccount'];
                            }
                        }
                        // comment by namnh
                        // else if (
                        //     (objcet1['debitAccount'] === null || objcet1['debitAccount'] === undefined
                        //         ? ''
                        //         : <string>objcet1['debitAccount']
                        //     ).startsWith('3331', 0)
                        // )
                        // {
                        //     /*Tính tổng tiền có tài khoản bắt đầu 3331*/
                        //     for (let i = 0; i < objectDT1.length; i++) {
                        //         const t = <string>objectDT1[i]['debitAccount'];
                        //         if (t !== undefined) {
                        //             if (
                        //                 (<string>objectDT1[i]['debitAccount'] === null ? '' : <string>objectDT1[i]['debitAccount']).startsWith(
                        //                     '3331',
                        //                     0
                        //                 )
                        //             ) {
                        //                 totalVat3331 = parseFloat(totalVat3331.toString()) + parseFloat(objectDT1[i]['amount'].toString());
                        //                 totalVat3331Original =
                        //                     parseFloat(totalVat3331Original.toString()) + parseFloat(objectDT1[i]['amountOriginal'].toString());
                        //             }
                        //         }
                        //     }
                        //     isVat3331 = true;
                        //     if (vat3331Account === '' || vat3331Account === null) {
                        //         vat3331Account = objcet1['debitAccount'];
                        //     }
                        // }
                    }
                    /** Tự động thêm thuế bên tag thuế với tài khoản bắt đầu 133 hoặc 3331 */
                    if (isVat133And3331) {
                        const ob = objectDT2.find(n => <string>n['vATAccount'] === null);
                        if (ob !== null && ob !== undefined) {
                            /*ob['creditAccount'] = vat133Account;*/
                            ob['vATAmount'] = totalVat133And3331;
                            ob['vATAmountOriginal'] = totalVat133And3331Origianl;
                            // add by namnh
                            if (ob['vATRate'] === 1) {
                                ob['pretaxAmount'] = parseFloat(totalVat133And3331.toString()) / 0.05;
                            } else if (ob['vATRate'] === 2) {
                                ob['pretaxAmount'] = parseFloat(totalVat133And3331.toString()) / 0.1;
                            } else {
                                ob['pretaxAmount'] = 0;
                            }
                        } else {
                            objectDT2.push({});
                            objectDT2[objectDT2.length - 1]['description'] = 'Thuế GTGT';
                            objectDT2[objectDT2.length - 1]['vATAccount'] = null;
                            objectDT2[objectDT2.length - 1]['vATAmount'] = totalVat133And3331;
                            objectDT2[objectDT2.length - 1]['vATAmountOriginal'] = totalVat133And3331Origianl;
                            objectDT2[objectDT2.length - 1]['accountingObjectID'] = objectParent['accountingObjectID'];
                            objectDT2[objectDT2.length - 1]['accountingObjectName'] = objectParent['accountingObjectName'];
                            objectDT2[objectDT2.length - 1]['taxCode'] = objectParent['taxCode'];
                            objectDT2[objectDT2.length - 1]['vATRate'] = 0;
                            objectDT2[objectDT2.length - 1]['pretaxAmount'] = 0;
                            objectDT2[objectDT2.length - 1]['pretaxAmountOriginal'] = 0;
                        }
                    } else if (isVat133) {
                        const ob = objectDT2.find(n => <string>n['vATAccount'] === vat133Account);
                        if (ob !== null && ob !== undefined) {
                            /*ob['creditAccount'] = vat133Account;*/
                            ob['vATAmount'] = totalVat133;
                            ob['vATAmountOriginal'] = totalVat133Original;
                            // add by namnh
                            if (ob['vATRate'] === 1) {
                                ob['pretaxAmount'] = parseFloat(totalVat133.toString()) / 0.05;
                                if (ob['pretaxAmount'] && objectParent['exchangeRate']) {
                                    ob['pretaxAmountOriginal'] = parseFloat(ob['pretaxAmount']) / parseFloat(exchangeRate.toString());
                                }
                            } else if (ob['vATRate'] === 2) {
                                ob['pretaxAmount'] = parseFloat(totalVat133.toString()) / 0.1;
                                if (ob['pretaxAmount'] && objectParent['exchangeRate']) {
                                    ob['pretaxAmountOriginal'] = parseFloat(ob['pretaxAmount']) / parseFloat(exchangeRate.toString());
                                }
                            } else {
                                ob['pretaxAmount'] = 0;
                                ob['pretaxAmountOriginal'] = 0;
                            }
                            // ob['pretaxAmount'] = parseFloat(totalVat133.toString())/0.05;
                        } else {
                            objectDT2.push({});
                            objectDT2[objectDT2.length - 1]['description'] = 'Thuế GTGT';
                            objectDT2[objectDT2.length - 1]['vATAccount'] = vat133Account;
                            objectDT2[objectDT2.length - 1]['vATAmount'] = totalVat133;
                            objectDT2[objectDT2.length - 1]['vATAmountOriginal'] = totalVat133Original;
                            // add by namnh
                            objectDT2[objectDT2.length - 1]['accountingObjectID'] = objectParent['accountingObjectID'];
                            objectDT2[objectDT2.length - 1]['accountingObjectName'] = objectParent['accountingObjectName'];
                            objectDT2[objectDT2.length - 1]['taxCode'] = objectParent['taxCode'];
                            objectDT2[objectDT2.length - 1]['vATRate'] = 0;
                            objectDT2[objectDT2.length - 1]['pretaxAmount'] = 0;
                            objectDT2[objectDT2.length - 1]['pretaxAmountOriginal'] = 0;
                        }
                    } else if (isVat3331) {
                        const ob = objectDT2.find(n => <string>n['vATAccount'] === vat3331Account);
                        if (ob !== null && ob !== undefined) {
                            /*ob['creditAccount'] = vat133Account;*/
                            ob['vATAmount'] = totalVat3331;
                            ob['vATAmountOriginal'] = totalVat3331Original;
                            // add by namnh
                            if (ob['vATRate'] === 1) {
                                ob['pretaxAmount'] = parseFloat(totalVat133.toString()) / 0.05;
                            } else if (ob['vATRate'] === 2) {
                                ob['pretaxAmount'] = parseFloat(totalVat133.toString()) / 0.1;
                            } else {
                                ob['pretaxAmount'] = 0;
                            }
                        } else {
                            objectDT2.push({});
                            objectDT2[objectDT2.length - 1]['description'] = 'Thuế GTGT';
                            objectDT2[objectDT2.length - 1]['vATAccount'] = vat3331Account;
                            objectDT2[objectDT2.length - 1]['vATAmount'] = totalVat3331;
                            objectDT2[objectDT2.length - 1]['vATAmountOriginal'] = totalVat3331Original;
                            objectDT2[objectDT2.length - 1]['accountingObjectID'] = objectParent['accountingObjectID'];
                            objectDT2[objectDT2.length - 1]['accountingObjectName'] = objectParent['accountingObjectName'];
                            objectDT2[objectDT2.length - 1]['taxCode'] = objectParent['taxCode'];
                            objectDT2[objectDT2.length - 1]['vATRate'] = 0;
                            objectDT2[objectDT2.length - 1]['pretaxAmount'] = 0;
                            objectDT2[objectDT2.length - 1]['pretaxAmountOriginal'] = 0;
                        }
                    }
                } else {
                }
                /*Tính tổng tiền nguyên tệ và quy đổi*/
                objectParent['totalAmount'] = 0;
                objectParent['totalAmountOriginal'] = 0;
                for (let i = 0; i < objectDT1.length; i++) {
                    objectParent['totalAmount'] = parseFloat(objectParent['totalAmount']) + parseFloat(objectDT1[i]['amount']);
                    objectParent['totalAmountOriginal'] =
                        parseFloat(objectParent['totalAmountOriginal']) + parseFloat(objectDT1[i]['amountOriginal']);
                }
                /*------------------*/
                // endregion
            } else {
                /*Tính tiền quy đổi*/
                objcet1['amount'] = this.round(
                    parseFloat(objcet1['amountOriginal']) * parseFloat(exchangeRate.toString()),
                    account.systemOption,
                    7
                );
                // region Các TH tính thuế có phát sinh TK Nợ và TK có
                if (objcet1.hasOwnProperty('debitAccount') || objcet1.hasOwnProperty('creditAccount')) {
                    let totalVat133 = 0;
                    let totalVat133Original = 0;
                    let totalVat133And3331 = 0;
                    let totalVat133And3331Origianl = 0;
                    let totalVat3331 = 0;
                    let totalVat3331Original = 0;
                    const totalVat = 0;
                    const totalVatOriginal = 0;
                    let isVat133 = false;
                    let isVat3331 = false;
                    let isVat133And3331 = false;
                    const isVat = false;
                    let vat133Account = '';
                    let vat3331Account = '';
                    const vat133And3331Account = '';
                    const vatAccount = '';
                    // trường hợp tính thuế GOtherVoucher
                    // edit by namnh
                    if (this.listTypeByCreditAccount.find(n => n.valueOf() === typeID)) {
                        if (
                            (objcet1['creditAccount'] === null || objcet1['creditAccount'] === undefined
                                ? ''
                                : <string>objcet1['creditAccount']
                            ).startsWith('3331', 0) &&
                            (objcet1['debitAccount'] === null || objcet1['debitAccount'] === undefined
                                ? ''
                                : <string>objcet1['debitAccount']
                            ).startsWith('133', 0)
                        ) {
                            /*Tính tổng tiền có tài khoản bắt đầu 3331 và 133*/
                            for (let i = 0; i < objectDT1.length; i++) {
                                const t = <string>objectDT1[i]['creditAccount'];
                                const t2 = <string>objectDT1[i]['debitAccount'];
                                if (t && t2) {
                                    if (
                                        (<string>objectDT1[i]['creditAccount'] === null ? '' : <string>objectDT1[i]['creditAccount']) ===
                                            <string>objcet1['creditAccount'] &&
                                        (<string>objectDT1[i]['debitAccount'] === null ? '' : <string>objectDT1[i]['debitAccount']) ===
                                            <string>objcet1['debitAccount']
                                    ) {
                                        totalVat133And3331 = this.round(
                                            parseFloat(totalVat133And3331.toString()) + parseFloat(objectDT1[i]['amount'].toString()),
                                            account.systemOption,
                                            7
                                        );
                                        totalVat133And3331Origianl = this.round(
                                            parseFloat(totalVat133And3331Origianl.toString()) +
                                                parseFloat(objectDT1[i]['amountOriginal'].toString()),
                                            account.systemOption,
                                            typeOriginal
                                        );
                                    }
                                }
                            }
                            isVat133And3331 = true;
                            // if (vat133And3331Account === '' || vat133And3331Account === null) {
                            //     vat133And3331Account = objcet1['creditAccount'];
                            // }
                        } else if (
                            (objcet1['creditAccount'] === null || objcet1['creditAccount'] === undefined
                                ? ''
                                : <string>objcet1['creditAccount']
                            ).startsWith('3331', 0)
                        ) {
                            /*Tính tổng tiền có tài khoản bắt đầu 3331*/
                            for (let i = 0; i < objectDT1.length; i++) {
                                const t = <string>objectDT1[i]['creditAccount'];
                                const t2 = <string>objectDT1[i]['debitAccount'];
                                let check133And3331debitAccount = false;
                                if (t2) {
                                    check133And3331debitAccount = t2.startsWith('133', 0);
                                }
                                if (t !== undefined && !check133And3331debitAccount) {
                                    if (
                                        (<string>objectDT1[i]['creditAccount'] === null ? '' : <string>objectDT1[i]['creditAccount']) ===
                                        <string>objcet1['creditAccount']
                                    ) {
                                        totalVat3331 = this.round(
                                            parseFloat(totalVat3331.toString()) + parseFloat(objectDT1[i]['amount'].toString()),
                                            account.systemOption,
                                            7
                                        );
                                        totalVat3331Original = this.round(
                                            parseFloat(totalVat3331Original.toString()) +
                                                parseFloat(objectDT1[i]['amountOriginal'].toString()),
                                            account.systemOption,
                                            typeOriginal
                                        );
                                    }
                                }
                            }
                            isVat3331 = true;
                            if (vat3331Account === '' || vat3331Account === null) {
                                vat3331Account = objcet1['creditAccount'];
                            }
                        } else if (
                            (objcet1['debitAccount'] === null || objcet1['debitAccount'] === undefined
                                ? ''
                                : <string>objcet1['debitAccount']
                            ).startsWith('133', 0)
                        ) {
                            /*Tính tổng tiền có tài khoản bắt đầu 133*/
                            for (let i = 0; i < objectDT1.length; i++) {
                                const t = <string>objectDT1[i]['debitAccount'];
                                let check133And3331creditAccount = false;
                                const t2 = <string>objectDT1[i]['creditAccount'];
                                if (t2) {
                                    check133And3331creditAccount = t2.startsWith('3331', 0);
                                }
                                if (t !== undefined && !check133And3331creditAccount) {
                                    if (
                                        (<string>objectDT1[i]['debitAccount'] === null ? '' : <string>objectDT1[i]['debitAccount']) ===
                                        <string>objcet1['debitAccount']
                                    ) {
                                        totalVat133 = this.round(
                                            parseFloat(totalVat133.toString()) + parseFloat(objectDT1[i]['amount'].toString()),
                                            account.systemOption,
                                            7
                                        );
                                        totalVat133Original = this.round(
                                            parseFloat(totalVat133Original.toString()) +
                                                parseFloat(objectDT1[i]['amountOriginal'].toString()),
                                            account.systemOption,
                                            typeOriginal
                                        );
                                    }
                                }
                            }
                            isVat133 = true;
                            if (vat133Account === '' || vat133Account === null) {
                                vat133Account = objcet1['debitAccount'];
                            }
                        }
                    } else if (this.listTypeByDebitAccount.find(n => n.valueOf() === typeID)) {
                        if (
                            (objcet1['debitAccount'] === null || objcet1['debitAccount'] === undefined
                                ? ''
                                : <string>objcet1['debitAccount']
                            ).startsWith('133', 0)
                        ) {
                            /*Tính tổng tiền có tài khoản bắt đầu 133*/
                            for (let i = 0; i < objectDT1.length; i++) {
                                const t = <string>objectDT1[i]['debitAccount'];
                                if (t !== undefined) {
                                    if (
                                        (<string>objectDT1[i]['debitAccount'] === null ? '' : <string>objectDT1[i]['debitAccount']) ===
                                        <string>objcet1['debitAccount']
                                    ) {
                                        totalVat133 = this.round(
                                            parseFloat(totalVat133.toString()) + parseFloat(objectDT1[i]['amount']),
                                            account.systemOption,
                                            7
                                        );
                                        totalVat133Original = this.round(
                                            parseFloat(totalVat133Original.toString()) +
                                                parseFloat(objectDT1[i]['amountOriginal'].toString()),
                                            account.systemOption,
                                            typeOriginal
                                        );
                                    }
                                }
                            }
                            isVat133 = true;
                            if (vat133Account === '' || vat133Account === null) {
                                vat133Account = objcet1['debitAccount'];
                            }
                        }
                        // comment by namnh
                        // else if (
                        //     (objcet1['debitAccount'] === null || objcet1['debitAccount'] === undefined
                        //         ? ''
                        //         : <string>objcet1['debitAccount']
                        //     ).startsWith('3331', 0)
                        // )
                        // {
                        //     /*Tính tổng tiền có tài khoản bắt đầu 3331*/
                        //     for (let i = 0; i < objectDT1.length; i++) {
                        //         const t = <string>objectDT1[i]['debitAccount'];
                        //         if (t !== undefined) {
                        //             if (
                        //                 (<string>objectDT1[i]['debitAccount'] === null ? '' : <string>objectDT1[i]['debitAccount']).startsWith(
                        //                     '3331',
                        //                     0
                        //                 )
                        //             ) {
                        //                 totalVat3331 = parseFloat(totalVat3331.toString()) + parseFloat(objectDT1[i]['amount'].toString());
                        //                 totalVat3331Original =
                        //                     parseFloat(totalVat3331Original.toString()) + parseFloat(objectDT1[i]['amountOriginal'].toString());
                        //             }
                        //         }
                        //     }
                        //     isVat3331 = true;
                        //     if (vat3331Account === '' || vat3331Account === null) {
                        //         vat3331Account = objcet1['debitAccount'];
                        //     }
                        // }
                    }
                    /** Tự động thêm thuế bên tag thuế với tài khoản bắt đầu 133 hoặc 3331 */
                    if (isVat133And3331) {
                        const ob = objectDT2.find(n => <string>n['vATAccount'] === null);
                        if (ob !== null && ob !== undefined) {
                            /*ob['creditAccount'] = vat133Account;*/
                            ob['vATAmount'] = totalVat133And3331;
                            ob['vATAmountOriginal'] = totalVat133And3331Origianl;
                            // add by namnh
                            if (ob['vATRate'] === 1) {
                                ob['pretaxAmount'] = this.round(parseFloat(totalVat133And3331.toString()) / 0.05, account.systemOption, 7);
                            } else if (ob['vATRate'] === 2) {
                                ob['pretaxAmount'] = this.round(parseFloat(totalVat133And3331.toString()) / 0.1, account.systemOption, 7);
                            } else {
                                ob['pretaxAmount'] = 0;
                            }
                        } else {
                            objectDT2.push({});
                            objectDT2[objectDT2.length - 1]['description'] = 'Thuế GTGT';
                            objectDT2[objectDT2.length - 1]['vATAccount'] = null;
                            objectDT2[objectDT2.length - 1]['vATAmount'] = totalVat133And3331;
                            objectDT2[objectDT2.length - 1]['vATAmountOriginal'] = totalVat133And3331Origianl;
                            objectDT2[objectDT2.length - 1]['accountingObjectID'] = objectParent['accountingObjectID'];
                            objectDT2[objectDT2.length - 1]['accountingObjectName'] = objectParent['accountingObjectName'];
                            objectDT2[objectDT2.length - 1]['taxCode'] = objectParent['taxCode'];
                            objectDT2[objectDT2.length - 1]['vATRate'] = 0;
                            objectDT2[objectDT2.length - 1]['pretaxAmount'] = 0;
                            objectDT2[objectDT2.length - 1]['pretaxAmountOriginal'] = 0;
                        }
                    } else if (isVat133) {
                        const ob = objectDT2.find(n => <string>n['vATAccount'] === vat133Account);
                        if (ob !== null && ob !== undefined) {
                            /*ob['creditAccount'] = vat133Account;*/
                            ob['vATAmount'] = totalVat133;
                            ob['vATAmountOriginal'] = totalVat133Original;
                            // add by namnh
                            if (ob['vATRate'] === 1) {
                                ob['pretaxAmount'] = this.round(parseFloat(totalVat133.toString()) / 0.05, account.systemOption, 7);
                                if (ob['pretaxAmount'] && objectParent['exchangeRate']) {
                                    ob['pretaxAmountOriginal'] = this.round(
                                        parseFloat(ob['pretaxAmount']) / parseFloat(exchangeRate.toString()),
                                        account.systemOption,
                                        typeOriginal
                                    );
                                }
                            } else if (ob['vATRate'] === 2) {
                                ob['pretaxAmount'] = this.round(parseFloat(totalVat133.toString()) / 0.1, account.systemOption, 7);
                                if (ob['pretaxAmount'] && objectParent['exchangeRate']) {
                                    ob['pretaxAmountOriginal'] = this.round(
                                        parseFloat(ob['pretaxAmount']) / parseFloat(exchangeRate.toString()),
                                        account.systemOption,
                                        typeOriginal
                                    );
                                }
                            } else {
                                ob['pretaxAmount'] = 0;
                                ob['pretaxAmountOriginal'] = 0;
                            }
                            // ob['pretaxAmount'] = parseFloat(totalVat133.toString())/0.05;
                        } else {
                            objectDT2.push({});
                            objectDT2[objectDT2.length - 1]['description'] = 'Thuế GTGT';
                            objectDT2[objectDT2.length - 1]['vATAccount'] = vat133Account;
                            objectDT2[objectDT2.length - 1]['vATAmount'] = totalVat133;
                            objectDT2[objectDT2.length - 1]['vATAmountOriginal'] = totalVat133Original;
                            // add by namnh
                            objectDT2[objectDT2.length - 1]['accountingObjectID'] = objectParent['accountingObjectID'];
                            objectDT2[objectDT2.length - 1]['accountingObjectName'] = objectParent['accountingObjectName'];
                            objectDT2[objectDT2.length - 1]['taxCode'] = objectParent['taxCode'];
                            objectDT2[objectDT2.length - 1]['vATRate'] = 0;
                            objectDT2[objectDT2.length - 1]['pretaxAmount'] = 0;
                            objectDT2[objectDT2.length - 1]['pretaxAmountOriginal'] = 0;
                        }
                    } else if (isVat3331) {
                        const ob = objectDT2.find(n => <string>n['vATAccount'] === vat3331Account);
                        if (ob !== null && ob !== undefined) {
                            /*ob['creditAccount'] = vat133Account;*/
                            ob['vATAmount'] = totalVat3331;
                            ob['vATAmountOriginal'] = totalVat3331Original;
                            // add by namnh
                            if (ob['vATRate'] === 1) {
                                ob['pretaxAmount'] = this.round(parseFloat(totalVat133.toString()) / 0.05, account.systemOption, 7);
                            } else if (ob['vATRate'] === 2) {
                                ob['pretaxAmount'] = this.round(parseFloat(totalVat133.toString()) / 0.1, account.systemOption, 7);
                            } else {
                                ob['pretaxAmount'] = 0;
                            }
                        } else {
                            objectDT2.push({});
                            objectDT2[objectDT2.length - 1]['description'] = 'Thuế GTGT';
                            objectDT2[objectDT2.length - 1]['vATAccount'] = vat3331Account;
                            objectDT2[objectDT2.length - 1]['vATAmount'] = totalVat3331;
                            objectDT2[objectDT2.length - 1]['vATAmountOriginal'] = totalVat3331Original;
                            objectDT2[objectDT2.length - 1]['accountingObjectID'] = objectParent['accountingObjectID'];
                            objectDT2[objectDT2.length - 1]['accountingObjectName'] = objectParent['accountingObjectName'];
                            objectDT2[objectDT2.length - 1]['taxCode'] = objectParent['taxCode'];
                            objectDT2[objectDT2.length - 1]['vATRate'] = 0;
                            objectDT2[objectDT2.length - 1]['pretaxAmount'] = 0;
                            objectDT2[objectDT2.length - 1]['pretaxAmountOriginal'] = 0;
                        }
                    }
                } else {
                }
                /*Tính tổng tiền nguyên tệ và quy đổi*/
                objectParent['totalAmount'] = 0;
                objectParent['totalAmountOriginal'] = 0;
                for (let i = 0; i < objectDT1.length; i++) {
                    objectParent['totalAmount'] = this.round(
                        parseFloat(objectParent['totalAmount']) + parseFloat(objectDT1[i]['amount']),
                        account.systemOption,
                        7
                    );
                    objectParent['totalAmountOriginal'] = this.round(
                        parseFloat(objectParent['totalAmountOriginal']) + parseFloat(objectDT1[i]['amountOriginal']),
                        account.systemOption,
                        typeOriginal
                    );
                }
                /*------------------*/
                // endregion
            }
        }
    }

    // region Định khoản tự động
    /*Author: Hautv
    *
    * */
    public autoPrinciple(autoPrinciple: IAutoPrinciple, detail: object) {
        if (autoPrinciple !== null && autoPrinciple !== undefined) {
            // if(detail.hasOwnProperty('creditAccount')){
            detail['creditAccount'] = autoPrinciple.creditAccount;
            // }
            // if(detail.hasOwnProperty('debitAccount')){
            detail['debitAccount'] = autoPrinciple.debitAccount;
            // }
        }
    }

    // endregion
    /*
    * Author Hautv
    * Get flie báo cáo
    * */
    public getCustomerReportPDF(req?: any, isDownload?: boolean) {
        const options = createRequestOption(req);
        let headers = new HttpHeaders();
        headers = headers.set('Accept', 'application/pdf');
        const respone = this.http.get(SERVER_API_URL + 'api/report/pdf', {
            params: options,
            observe: 'response',
            headers,
            responseType: 'blob'
        });
        respone.subscribe(response => {
            // this.showReport(response);
            const file = new Blob([response.body], { type: 'application/pdf' });
            const fileURL = window.URL.createObjectURL(file);
            if (isDownload) {
                const link = document.createElement('a');
                document.body.appendChild(link);
                link.download = fileURL;
                link.setAttribute('style', 'display: none');
                const name = 'ten_bao_cao.pdf';
                link.setAttribute('download', name);
                link.href = fileURL;
                link.click();
            } else {
                const contentDispositionHeader = response.headers.get('Content-Disposition');
                const result = contentDispositionHeader
                    .split(';')[1]
                    .trim()
                    .split('=')[1];
                const newWin = window.open(fileURL, '_blank');
                // add a load listener to the window so that the title gets changed on page load

                newWin.addEventListener('load', () => {
                    newWin.document.title = result.replace(/"/g, '');
                    // this.router.navigate(['/report/buy']);
                });
            }
        });
    }

    /*
    * @author anmt
    * right click event
    * */

    // activates the menu with the coordinates
    public onrightClick(
        event,
        eventData: any,
        selectedObj: any,
        isShowTab1: any,
        isShowTab2: any,
        contextmenu: any,
        x: any,
        y: any,
        select: number
    ) {
        if (select === 0) {
            selectedObj.value = eventData;
            isShowTab1.value = true;
            isShowTab2.value = false;
            // prevent overflow page
            if (event.pageX > 1290) {
                x.value = event.pageX - 200;
            } else if (event.pageX < 1290) {
                x.value = event.pageX;
            }
            y.value = event.pageY;
            contextmenu.value = true;
        } else if (select === 1) {
            selectedObj.value = eventData;
            isShowTab1.value = false;
            isShowTab2.value = true;
            // prevent overflow page
            if (event.pageX > 1290) {
                x.value = event.pageX - 200;
            } else if (event.pageX < 1290) {
                x.value = event.pageX;
            }
            y.value = event.pageY;
            contextmenu.value = true;
        }
    }

    //  end of right click

    /*Hautv*/
    findByRowNum(req?: any): Observable<EntityResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<object>(SERVER_API_URL + 'api/voucher/findByRowNum', {
                params: options,
                observe: 'response'
            })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    /*hautv*/
    getIndexRow(req?: any): Observable<EntityResponseType> {
        const options = createRequestOption(req);
        return this.http.get<object>(SERVER_API_URL + 'api/voucher/getIndexVoucher', {
            params: options,
            observe: 'response'
        });
    }

    searchAll(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<object[]>(SERVER_API_URL + 'api/voucher/search', { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    // Add by Hautv
    exportPDF(req?: any) {
        const options = createRequestOption(req);
        let headers = new HttpHeaders();
        headers = headers.set('Accept', 'application/x-excel');
        return this.http.get(SERVER_API_URL + 'api/export/pdf', {
            params: options,
            observe: 'response',
            headers,
            responseType: 'blob'
        });
    }

    // Add by Hautv
    exportExcel(req?: any): Observable<any> {
        const options = createRequestOption(req);
        let headers = new HttpHeaders();
        headers = headers.set('Accept', 'application/x-excel');
        return this.http.get(SERVER_API_URL + 'api/export/excel', {
            params: options,
            observe: 'response',
            headers,
            responseType: 'blob'
        });
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body['date'] = res.body['date'] != null ? moment(res.body['date']) : null;
        res.body['postedDate'] = res.body['postedDate'] != null ? moment(res.body['postedDate']) : null;
        res.body['issuedate'] = res.body['issuedate'] != null ? moment(res.body['issuedate']) : null;
        res.body['matchdate'] = res.body['matchdate'] != null ? moment(res.body['matchdate']) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((obj: object) => {
            obj['date'] = obj['date'] != null ? moment(obj['date']) : null;
            obj['postedDate'] = obj['postedDate'] != null ? moment(obj['postedDate']) : null;
            obj['issuedate'] = obj['issuedate'] != null ? moment(obj['issuedate']) : null;
            obj['matchdate'] = obj['matchdate'] != null ? moment(obj['matchdate']) : null;
        });
        return res;
    }

    // endregion

    /*
    * @author namnh
    * Sắp xếp hàng tiền, thuế theo Orderpriority
    * */
    public getSortArray(myArr: any[]) {
        if (myArr !== undefined && myArr !== null) {
            return myArr.sort((a1, a2) => {
                return a1.orderPriority - a2.orderPriority;
            });
        } else {
        }
    }

    /*
    * @author Hautv
    * Sinh số chứng từ tự động
    * */

    getGenCodeVoucher(req?: any): Observable<any> {
        const options = createRequestOption(req);
        return this.http.get(SERVER_API_URL + 'api/voucher/genCodeVoucher', {
            params: options,
            observe: 'response',
            responseType: 'text'
        });
    }

    /** @Author Hautv
     * Kiểm tra MST hợp lệ
     * *!/*/

    /*public checkMST(mst: string): boolean {
   * @Author Hautv
   * Kiểm tra MST hợp lệ
   * */
    public checkMST(mst: string): boolean {
        let istrue = false;
        if (mst.length <= 14 && mst.length >= 10) {
            const value =
                this.getInt(mst[0]) * 31 +
                this.getInt(mst[1]) * 29 +
                this.getInt(mst[2]) * 23 +
                this.getInt(mst[3]) * 19 +
                this.getInt(mst[4]) * 17 +
                this.getInt(mst[5]) * 13 +
                this.getInt(mst[6]) * 7 +
                this.getInt(mst[7]) * 5 +
                this.getInt(mst[8]) * 3;
            const mod = 10 - value % 11;
            if (Math.abs(mod) === this.getInt(mst[9])) {
                istrue = true;
            }
        } else {
            istrue = false;
        }
        return istrue;
    }

    public getInt(c) {
        let a = 0;
        a = c.toString().charCodeAt(0) - 48;
        return a;
    }

    /*----Check MST-----*/

    /*
* @author namnh
* Config combobox chọn mốc thời gian
* */
    getCbbTimeLine() {
        let listTimeLine;
        this.translate
            .get([
                'ebwebApp.receiveBill.timeLine.today',
                'ebwebApp.receiveBill.timeLine.thisWeek',
                'ebwebApp.receiveBill.timeLine.earlyWeekToNow',
                'ebwebApp.receiveBill.timeLine.thisMonth',
                'ebwebApp.receiveBill.timeLine.earlyMonthToNow',
                'ebwebApp.receiveBill.timeLine.thisQuarter',
                'ebwebApp.receiveBill.timeLine.earlyQuarterToNow',
                'ebwebApp.receiveBill.timeLine.thisYear',
                'ebwebApp.receiveBill.timeLine.earlyYearToNow',
                'ebwebApp.receiveBill.timeLine.january',
                'ebwebApp.receiveBill.timeLine.february',
                'ebwebApp.receiveBill.timeLine.march',
                'ebwebApp.receiveBill.timeLine.april',
                'ebwebApp.receiveBill.timeLine.may',
                'ebwebApp.receiveBill.timeLine.june',
                'ebwebApp.receiveBill.timeLine.july',
                'ebwebApp.receiveBill.timeLine.august',
                'ebwebApp.receiveBill.timeLine.september',
                'ebwebApp.receiveBill.timeLine.october',
                'ebwebApp.receiveBill.timeLine.november',
                'ebwebApp.receiveBill.timeLine.december',
                'ebwebApp.receiveBill.timeLine.quarterOne',
                'ebwebApp.receiveBill.timeLine.quarterTwo',
                'ebwebApp.receiveBill.timeLine.quarterThree',
                'ebwebApp.receiveBill.timeLine.quarterFour',
                'ebwebApp.receiveBill.timeLine.lastWeek',
                'ebwebApp.receiveBill.timeLine.lastMonth',
                'ebwebApp.receiveBill.timeLine.lastQuarter',
                'ebwebApp.receiveBill.timeLine.lastYear',
                'ebwebApp.receiveBill.timeLine.nextWeek',
                'ebwebApp.receiveBill.timeLine.theNextFourWeeks',
                'ebwebApp.receiveBill.timeLine.nextMonth',
                'ebwebApp.receiveBill.timeLine.nextQuarter',
                'ebwebApp.receiveBill.timeLine.nextYear',
                'ebwebApp.receiveBill.timeLine.optional'
            ])
            .subscribe(res => {
                listTimeLine = [
                    { value: '0', display: res['ebwebApp.receiveBill.timeLine.today'] },
                    { value: '1', display: res['ebwebApp.receiveBill.timeLine.thisWeek'] },
                    { value: '2', display: res['ebwebApp.receiveBill.timeLine.earlyWeekToNow'] },
                    { value: '3', display: res['ebwebApp.receiveBill.timeLine.thisMonth'] },
                    { value: '4', display: res['ebwebApp.receiveBill.timeLine.earlyMonthToNow'] },
                    { value: '5', display: res['ebwebApp.receiveBill.timeLine.thisQuarter'] },
                    { value: '6', display: res['ebwebApp.receiveBill.timeLine.earlyQuarterToNow'] },
                    { value: '7', display: res['ebwebApp.receiveBill.timeLine.thisYear'] },
                    { value: '8', display: res['ebwebApp.receiveBill.timeLine.earlyYearToNow'] },
                    { value: '9', display: res['ebwebApp.receiveBill.timeLine.january'] },
                    { value: '10', display: res['ebwebApp.receiveBill.timeLine.february'] },
                    { value: '11', display: res['ebwebApp.receiveBill.timeLine.march'] },
                    { value: '12', display: res['ebwebApp.receiveBill.timeLine.april'] },
                    { value: '13', display: res['ebwebApp.receiveBill.timeLine.may'] },
                    { value: '14', display: res['ebwebApp.receiveBill.timeLine.june'] },
                    { value: '15', display: res['ebwebApp.receiveBill.timeLine.july'] },
                    { value: '16', display: res['ebwebApp.receiveBill.timeLine.august'] },
                    { value: '17', display: res['ebwebApp.receiveBill.timeLine.september'] },
                    { value: '18', display: res['ebwebApp.receiveBill.timeLine.october'] },
                    { value: '19', display: res['ebwebApp.receiveBill.timeLine.november'] },
                    { value: '20', display: res['ebwebApp.receiveBill.timeLine.december'] },
                    { value: '21', display: res['ebwebApp.receiveBill.timeLine.quarterOne'] },
                    { value: '22', display: res['ebwebApp.receiveBill.timeLine.quarterTwo'] },
                    { value: '23', display: res['ebwebApp.receiveBill.timeLine.quarterThree'] },
                    { value: '24', display: res['ebwebApp.receiveBill.timeLine.quarterFour'] },
                    { value: '25', display: res['ebwebApp.receiveBill.timeLine.lastWeek'] },
                    { value: '26', display: res['ebwebApp.receiveBill.timeLine.lastMonth'] },
                    { value: '27', display: res['ebwebApp.receiveBill.timeLine.lastQuarter'] },
                    { value: '28', display: res['ebwebApp.receiveBill.timeLine.lastYear'] },
                    { value: '29', display: res['ebwebApp.receiveBill.timeLine.nextWeek'] },
                    { value: '30', display: res['ebwebApp.receiveBill.timeLine.theNextFourWeeks'] },
                    { value: '31', display: res['ebwebApp.receiveBill.timeLine.nextMonth'] },
                    { value: '32', display: res['ebwebApp.receiveBill.timeLine.nextQuarter'] },
                    { value: '33', display: res['ebwebApp.receiveBill.timeLine.nextYear'] },
                    { value: '34', display: res['ebwebApp.receiveBill.timeLine.optional'] }
                ];
            });
        return listTimeLine;
    }

    getCbbTimeLine2() {
        return new Promise(resolve => {
            this.translate
                .get([
                    'ebwebApp.receiveBill.timeLine.today',
                    'ebwebApp.receiveBill.timeLine.thisWeek',
                    'ebwebApp.receiveBill.timeLine.earlyWeekToNow',
                    'ebwebApp.receiveBill.timeLine.thisMonth',
                    'ebwebApp.receiveBill.timeLine.earlyMonthToNow',
                    'ebwebApp.receiveBill.timeLine.thisQuarter',
                    'ebwebApp.receiveBill.timeLine.earlyQuarterToNow',
                    'ebwebApp.receiveBill.timeLine.thisYear',
                    'ebwebApp.receiveBill.timeLine.earlyYearToNow',
                    'ebwebApp.receiveBill.timeLine.january',
                    'ebwebApp.receiveBill.timeLine.february',
                    'ebwebApp.receiveBill.timeLine.march',
                    'ebwebApp.receiveBill.timeLine.april',
                    'ebwebApp.receiveBill.timeLine.may',
                    'ebwebApp.receiveBill.timeLine.june',
                    'ebwebApp.receiveBill.timeLine.july',
                    'ebwebApp.receiveBill.timeLine.august',
                    'ebwebApp.receiveBill.timeLine.september',
                    'ebwebApp.receiveBill.timeLine.october',
                    'ebwebApp.receiveBill.timeLine.november',
                    'ebwebApp.receiveBill.timeLine.december',
                    'ebwebApp.receiveBill.timeLine.quarterOne',
                    'ebwebApp.receiveBill.timeLine.quarterTwo',
                    'ebwebApp.receiveBill.timeLine.quarterThree',
                    'ebwebApp.receiveBill.timeLine.quarterFour',
                    'ebwebApp.receiveBill.timeLine.lastWeek',
                    'ebwebApp.receiveBill.timeLine.lastMonth',
                    'ebwebApp.receiveBill.timeLine.lastQuarter',
                    'ebwebApp.receiveBill.timeLine.lastYear',
                    'ebwebApp.receiveBill.timeLine.nextWeek',
                    'ebwebApp.receiveBill.timeLine.theNextFourWeeks',
                    'ebwebApp.receiveBill.timeLine.nextMonth',
                    'ebwebApp.receiveBill.timeLine.nextQuarter',
                    'ebwebApp.receiveBill.timeLine.nextYear',
                    'ebwebApp.receiveBill.timeLine.optional'
                ])
                .subscribe(res => {
                    const listTimeLine = [
                        { value: '0', display: res['ebwebApp.receiveBill.timeLine.today'] },
                        { value: '1', display: res['ebwebApp.receiveBill.timeLine.thisWeek'] },
                        { value: '2', display: res['ebwebApp.receiveBill.timeLine.earlyWeekToNow'] },
                        { value: '3', display: res['ebwebApp.receiveBill.timeLine.thisMonth'] },
                        { value: '4', display: res['ebwebApp.receiveBill.timeLine.earlyMonthToNow'] },
                        { value: '5', display: res['ebwebApp.receiveBill.timeLine.thisQuarter'] },
                        { value: '6', display: res['ebwebApp.receiveBill.timeLine.earlyQuarterToNow'] },
                        { value: '7', display: res['ebwebApp.receiveBill.timeLine.thisYear'] },
                        { value: '8', display: res['ebwebApp.receiveBill.timeLine.earlyYearToNow'] },
                        { value: '9', display: res['ebwebApp.receiveBill.timeLine.january'] },
                        { value: '10', display: res['ebwebApp.receiveBill.timeLine.february'] },
                        { value: '11', display: res['ebwebApp.receiveBill.timeLine.march'] },
                        { value: '12', display: res['ebwebApp.receiveBill.timeLine.april'] },
                        { value: '13', display: res['ebwebApp.receiveBill.timeLine.may'] },
                        { value: '14', display: res['ebwebApp.receiveBill.timeLine.june'] },
                        { value: '15', display: res['ebwebApp.receiveBill.timeLine.july'] },
                        { value: '16', display: res['ebwebApp.receiveBill.timeLine.august'] },
                        { value: '17', display: res['ebwebApp.receiveBill.timeLine.september'] },
                        { value: '18', display: res['ebwebApp.receiveBill.timeLine.october'] },
                        { value: '19', display: res['ebwebApp.receiveBill.timeLine.november'] },
                        { value: '20', display: res['ebwebApp.receiveBill.timeLine.december'] },
                        { value: '21', display: res['ebwebApp.receiveBill.timeLine.quarterOne'] },
                        { value: '22', display: res['ebwebApp.receiveBill.timeLine.quarterTwo'] },
                        { value: '23', display: res['ebwebApp.receiveBill.timeLine.quarterThree'] },
                        { value: '24', display: res['ebwebApp.receiveBill.timeLine.quarterFour'] },
                        { value: '25', display: res['ebwebApp.receiveBill.timeLine.lastWeek'] },
                        { value: '26', display: res['ebwebApp.receiveBill.timeLine.lastMonth'] },
                        { value: '27', display: res['ebwebApp.receiveBill.timeLine.lastQuarter'] },
                        { value: '28', display: res['ebwebApp.receiveBill.timeLine.lastYear'] },
                        { value: '29', display: res['ebwebApp.receiveBill.timeLine.nextWeek'] },
                        { value: '30', display: res['ebwebApp.receiveBill.timeLine.theNextFourWeeks'] },
                        { value: '31', display: res['ebwebApp.receiveBill.timeLine.nextMonth'] },
                        { value: '32', display: res['ebwebApp.receiveBill.timeLine.nextQuarter'] },
                        { value: '33', display: res['ebwebApp.receiveBill.timeLine.nextYear'] },
                        { value: '34', display: res['ebwebApp.receiveBill.timeLine.optional'] }
                    ];
                    resolve(listTimeLine);
                });
        });
        // return listTimeLine;
    }

    /*
 * @author namnh
 * Tự động load từ ngày đến ngày khi chọn mốc thời gian
 * */
    getTimeLine(intTimeLine: String) {
        this.objTimeLine = {};
        const today = new Date();
        this.dtBeginTemp = new Date();
        this.dtEndTemp = new Date();
        const dayOfWeek = today.getDay();
        const month = today.getMonth();
        const year = today.getFullYear();
        // số ngày từ ngày đầu tuần tới ngày hiện tại
        // const alpha = getLocaleFirstDayOfWeek('');
        const alpha = 7 - dayOfWeek;
        switch (intTimeLine.toString()) {
            case '0': // hôm nay
                this.dtBeginDate = this.dtEndDate = this.datepipe.transform(today, 'yyyy-MM-dd');
                break;
            case '1': // tuần này
                this.dtBeginDate = this.datepipe.transform(
                    this.dtBeginTemp.setDate(this.dtBeginTemp.getDate() - (dayOfWeek - 1)),
                    'yyyy-MM-dd'
                );
                this.dtEndDate = this.datepipe.transform(this.dtEndTemp.setDate(this.dtEndTemp.getDate() + alpha), 'yyyy-MM-dd');
                break;
            case '2': // đầu tuần đến hiện tại
                this.dtBeginDate = this.datepipe.transform(today.setDate(today.getDate() - (dayOfWeek - 1)), 'yyyy-MM-dd');
                this.dtEndDate = this.datepipe.transform(this.dtEndTemp, 'yyyy-MM-dd');
                break;
            case '3': // tháng này
                this.dtBeginDate = this.datepipe.transform(new Date(year, month, 1), 'yyyy-MM-dd');
                this.dtEndDate = this.datepipe.transform(new Date(year, month + 1, 0), 'yyyy-MM-dd');
                break;
            case '4': // đầu tháng tới hiện tại
                this.dtBeginDate = this.datepipe.transform(new Date(year, month, 1), 'yyyy-MM-dd');
                this.dtEndDate = this.datepipe.transform(this.dtEndTemp, 'yyyy-MM-dd');
                break;
            case '5': // quý này
                // quý I
                if (month >= 0 && month <= 2) {
                    this.dtBeginDate = this.datepipe.transform(new Date(year, 0, 1), 'yyyy-MM-dd');
                    this.dtEndDate = this.datepipe.transform(new Date(year, 3, 0), 'yyyy-MM-dd');
                } else if (month >= 3 && month <= 5) {
                    // quý II
                    this.dtBeginDate = this.datepipe.transform(new Date(year, 3, 1), 'yyyy-MM-dd');
                    this.dtEndDate = this.datepipe.transform(new Date(year, 6, 0), 'yyyy-MM-dd');
                } else if (month >= 6 && month <= 8) {
                    // quý III
                    this.dtBeginDate = this.datepipe.transform(new Date(year, 6, 1), 'yyyy-MM-dd');
                    this.dtEndDate = this.datepipe.transform(new Date(year, 9, 0), 'yyyy-MM-dd');
                } else if (month >= 9 && month <= 11) {
                    // quý IV
                    this.dtBeginDate = this.datepipe.transform(new Date(year, 9, 1), 'yyyy-MM-dd');
                    this.dtEndDate = this.datepipe.transform(new Date(year, 12, 0), 'yyyy-MM-dd');
                } else {
                    this.toastr.error(
                        this.translate.instant('ebwebApp.receiveBill.errorWhenSelectTimeLine'),
                        this.translate.instant('ebwebApp.receiveBill.message')
                    );
                }
                break;
            case '6': // đầu quý đến hiện tại
                // quý I
                if (month >= 0 && month <= 2) {
                    this.dtBeginDate = this.datepipe.transform(new Date(year, 0, 1), 'yyyy-MM-dd');
                    this.dtEndDate = this.datepipe.transform(this.dtEndTemp, 'yyyy-MM-dd');
                } else if (month >= 3 && month <= 5) {
                    // quý II
                    this.dtBeginDate = this.datepipe.transform(new Date(year, 3, 1), 'yyyy-MM-dd');
                    this.dtEndDate = this.datepipe.transform(this.dtEndTemp, 'yyyy-MM-dd');
                } else if (month >= 6 && month <= 8) {
                    // quý III
                    this.dtBeginDate = this.datepipe.transform(new Date(year, 6, 1), 'yyyy-MM-dd');
                    this.dtEndDate = this.datepipe.transform(this.dtEndTemp, 'yyyy-MM-dd');
                } else if (month >= 9 && month <= 11) {
                    // quý IV
                    this.dtBeginDate = this.datepipe.transform(new Date(year, 9, 1), 'yyyy-MM-dd');
                    this.dtEndDate = this.datepipe.transform(this.dtEndTemp, 'yyyy-MM-dd');
                } else {
                    this.toastr.error(
                        this.translate.instant('ebwebApp.receiveBill.errorWhenSelectTimeLine'),
                        this.translate.instant('ebwebApp.receiveBill.message')
                    );
                }
                break;
            case '7': // năm nay
                this.dtBeginDate = this.datepipe.transform(new Date(year, 0, 1), 'yyyy-MM-dd');
                this.dtEndDate = this.datepipe.transform(new Date(year, 12, 0), 'yyyy-MM-dd');
                break;
            case '8': // đầu năm tới hiện tại
                this.dtBeginDate = this.datepipe.transform(new Date(year, 0, 1), 'yyyy-MM-dd');
                this.dtEndDate = this.datepipe.transform(this.dtEndTemp, 'yyyy-MM-dd');
                break;
            case '9': // tháng 1
                this.dtBeginDate = this.datepipe.transform(new Date(year, 0, 1), 'yyyy-MM-dd');
                this.dtEndDate = this.datepipe.transform(new Date(year, 1, 0), 'yyyy-MM-dd');
                break;
            case '10': // tháng 2
                this.dtBeginDate = this.datepipe.transform(new Date(year, 1, 1), 'yyyy-MM-dd');
                this.dtEndDate = this.datepipe.transform(new Date(year, 2, 0), 'yyyy-MM-dd');
                break;
            case '11': // tháng 3
                this.dtBeginDate = this.datepipe.transform(new Date(year, 2, 1), 'yyyy-MM-dd');
                this.dtEndDate = this.datepipe.transform(new Date(year, 3, 0), 'yyyy-MM-dd');
                break;
            case '12': // tháng 4
                this.dtBeginDate = this.datepipe.transform(new Date(year, 3, 1), 'yyyy-MM-dd');
                this.dtEndDate = this.datepipe.transform(new Date(year, 4, 0), 'yyyy-MM-dd');
                break;
            case '13': // tháng 5
                this.dtBeginDate = this.datepipe.transform(new Date(year, 4, 1), 'yyyy-MM-dd');
                this.dtEndDate = this.datepipe.transform(new Date(year, 5, 0), 'yyyy-MM-dd');
                break;
            case '14': // tháng 6
                this.dtBeginDate = this.datepipe.transform(new Date(year, 5, 1), 'yyyy-MM-dd');
                this.dtEndDate = this.datepipe.transform(new Date(year, 6, 0), 'yyyy-MM-dd');
                break;
            case '15': // tháng 7
                this.dtBeginDate = this.datepipe.transform(new Date(year, 6, 1), 'yyyy-MM-dd');
                this.dtEndDate = this.datepipe.transform(new Date(year, 7, 0), 'yyyy-MM-dd');
                break;
            case '16': // tháng 8
                this.dtBeginDate = this.datepipe.transform(new Date(year, 7, 1), 'yyyy-MM-dd');
                this.dtEndDate = this.datepipe.transform(new Date(year, 8, 0), 'yyyy-MM-dd');
                break;
            case '17': // tháng 9
                this.dtBeginDate = this.datepipe.transform(new Date(year, 8, 1), 'yyyy-MM-dd');
                this.dtEndDate = this.datepipe.transform(new Date(year, 9, 0), 'yyyy-MM-dd');
                break;
            case '18': // tháng 10
                this.dtBeginDate = this.datepipe.transform(new Date(year, 9, 1), 'yyyy-MM-dd');
                this.dtEndDate = this.datepipe.transform(new Date(year, 10, 0), 'yyyy-MM-dd');
                break;
            case '19': // tháng 11
                this.dtBeginDate = this.datepipe.transform(new Date(year, 10, 1), 'yyyy-MM-dd');
                this.dtEndDate = this.datepipe.transform(new Date(year, 11, 0), 'yyyy-MM-dd');
                break;
            case '20': // tháng 12
                this.dtBeginDate = this.datepipe.transform(new Date(year, 11, 1), 'yyyy-MM-dd');
                this.dtEndDate = this.datepipe.transform(new Date(year, 12, 0), 'yyyy-MM-dd');
                break;
            case '21': // quý I
                this.dtBeginDate = this.datepipe.transform(new Date(year, 0, 1), 'yyyy-MM-dd');
                this.dtEndDate = this.datepipe.transform(new Date(year, 3, 0), 'yyyy-MM-dd');
                break;
            case '22': // quý II
                this.dtBeginDate = this.datepipe.transform(new Date(year, 3, 1), 'yyyy-MM-dd');
                this.dtEndDate = this.datepipe.transform(new Date(year, 6, 0), 'yyyy-MM-dd');
                break;
            case '23': // quý III
                this.dtBeginDate = this.datepipe.transform(new Date(year, 6, 1), 'yyyy-MM-dd');
                this.dtEndDate = this.datepipe.transform(new Date(year, 9, 0), 'yyyy-MM-dd');
                break;
            case '24': // quý IV
                this.dtBeginDate = this.datepipe.transform(new Date(year, 9, 1), 'yyyy-MM-dd');
                this.dtEndDate = this.datepipe.transform(new Date(year, 12, 0), 'yyyy-MM-dd');
                break;
            case '25': // tuần trước
                this.dtBeginDate = this.datepipe.transform(
                    this.dtBeginTemp.setDate(this.dtBeginTemp.getDate() - (dayOfWeek - 1) - 7),
                    'yyyy-MM-dd'
                );
                this.dtEndDate = this.datepipe.transform(
                    this.dtEndTemp.setDate(this.dtEndTemp.getDate() - (dayOfWeek - 1) - 1),
                    'yyyy-MM-dd'
                );
                break;
            case '26': // tháng trước
                let lastMonth;
                let lastYear;
                if (month === 0) {
                    lastMonth = 12;
                    lastYear = year - 1;
                } else {
                    lastMonth = month - 1;
                    lastYear = year;
                }
                this.dtBeginDate = this.datepipe.transform(new Date(lastYear, lastMonth, 1), 'yyyy-MM-dd');
                this.dtEndDate = this.datepipe.transform(new Date(lastYear, lastMonth + 1, 0), 'yyyy-MM-dd');
                break;
            case '27': // quý trước
                // quý I
                if (month >= 0 && month <= 2) {
                    this.dtBeginDate = this.datepipe.transform(new Date(year - 1, 9, 1), 'yyyy-MM-dd');
                    this.dtEndDate = this.datepipe.transform(new Date(year - 1, 12, 0), 'yyyy-MM-dd');
                } else if (month >= 3 && month <= 5) {
                    // quý II
                    this.dtBeginDate = this.datepipe.transform(new Date(year, 0, 1), 'yyyy-MM-dd');
                    this.dtEndDate = this.datepipe.transform(new Date(year, 3, 0), 'yyyy-MM-dd');
                } else if (month >= 6 && month <= 8) {
                    // quý III
                    this.dtBeginDate = this.datepipe.transform(new Date(year, 3, 1), 'yyyy-MM-dd');
                    this.dtEndDate = this.datepipe.transform(new Date(year, 6, 0), 'yyyy-MM-dd');
                } else if (month >= 9 && month <= 11) {
                    // quý IV
                    this.dtBeginDate = this.datepipe.transform(new Date(year, 6, 1), 'yyyy-MM-dd');
                    this.dtEndDate = this.datepipe.transform(new Date(year, 9, 0), 'yyyy-MM-dd');
                } else {
                    this.toastr.error(
                        this.translate.instant('ebwebApp.receiveBill.errorWhenSelectTimeLine'),
                        this.translate.instant('ebwebApp.receiveBill.message')
                    );
                }
                break;
            case '28': // năm trước
                this.dtBeginDate = this.datepipe.transform(new Date(year - 1, 0, 1), 'yyyy-MM-dd');
                this.dtEndDate = this.datepipe.transform(new Date(year - 1, 12, 0), 'yyyy-MM-dd');
                break;
            case '29': // tuần sau
                this.dtBeginDate = this.datepipe.transform(
                    this.dtBeginTemp.setDate(this.dtBeginTemp.getDate() + (alpha + 1)),
                    'yyyy-MM-dd'
                );
                this.dtEndDate = this.datepipe.transform(this.dtBeginTemp.setDate(this.dtBeginTemp.getDate() + 6), 'yyyy-MM-dd');
                break;
            case '30': // bốn tuần tới
                this.dtBeginDate = this.datepipe.transform(
                    this.dtBeginTemp.setDate(this.dtBeginTemp.getDate() + (alpha + 1)),
                    'yyyy-MM-dd'
                );
                this.dtEndDate = this.datepipe.transform(this.dtBeginTemp.setDate(this.dtBeginTemp.getDate() + 27), 'yyyy-MM-dd');
                break;
            case '31': // tháng sau
                let afterMonth;
                let afterYear;
                if (month === 11) {
                    afterMonth = 0;
                    afterYear = year + 1;
                } else {
                    afterMonth = month + 1;
                    afterYear = year;
                }
                this.dtBeginDate = this.datepipe.transform(new Date(afterYear, afterMonth, 1), 'yyyy-MM-dd');
                this.dtEndDate = this.datepipe.transform(new Date(afterYear, afterMonth + 1, 0), 'yyyy-MM-dd');
                break;
            case '32': // quý sau
                // quý I
                if (month >= 0 && month <= 2) {
                    this.dtBeginDate = this.datepipe.transform(new Date(year, 3, 1), 'yyyy-MM-dd');
                    this.dtEndDate = this.datepipe.transform(new Date(year, 6, 0), 'yyyy-MM-dd');
                } else if (month >= 3 && month <= 5) {
                    // quý II
                    this.dtBeginDate = this.datepipe.transform(new Date(year, 6, 1), 'yyyy-MM-dd');
                    this.dtEndDate = this.datepipe.transform(new Date(year, 9, 0), 'yyyy-MM-dd');
                } else if (month >= 6 && month <= 8) {
                    // quý III
                    this.dtBeginDate = this.datepipe.transform(new Date(year, 9, 1), 'yyyy-MM-dd');
                    this.dtEndDate = this.datepipe.transform(new Date(year, 12, 0), 'yyyy-MM-dd');
                } else if (month >= 9 && month <= 11) {
                    // quý IV
                    this.dtBeginDate = this.datepipe.transform(new Date(year + 1, 0, 1), 'yyyy-MM-dd');
                    this.dtEndDate = this.datepipe.transform(new Date(year + 1, 3, 0), 'yyyy-MM-dd');
                } else {
                    this.toastr.error(
                        this.translate.instant('ebwebApp.receiveBill.errorWhenSelectTimeLine'),
                        this.translate.instant('ebwebApp.receiveBill.message')
                    );
                }
                break;
            case '33': // năm sau
                this.dtBeginDate = this.datepipe.transform(new Date(year + 1, 0, 1), 'yyyy-MM-dd');
                this.dtEndDate = this.datepipe.transform(new Date(year + 1, 12, 0), 'yyyy-MM-dd');
                break;
            case '34': // Tùy chọn
                this.dtBeginDate = this.datepipe.transform(new Date(), 'yyyy-MM-dd');
                this.dtEndDate = this.datepipe.transform(new Date(), 'yyyy-MM-dd');
                break;
        }
        this.objTimeLine.dtBeginDate = this.dtBeginDate;
        this.objTimeLine.dtEndDate = this.dtEndDate;
        return this.objTimeLine;
    }

    /*
    * Author Hautv
    * return Ngày hạch toán
    * ac : Account
    * */
    ngayHachToan(ac): Moment {
        const now = moment(new Date(), DATE_FORMAT);
        if (ac) {
            const ngayHachToanDefault = moment(ac.systemOption.find(x => x.code === NGAY_HACH_TOAN).defaultData, DATE_FORMAT);
            const dd = ngayHachToanDefault.toDate();
            const ngayHachToanData = moment(ac.systemOption.find(x => x.code === NGAY_HACH_TOAN).data, DATE_FORMAT);
            if (ngayHachToanDefault.format(DATE_FORMAT) === moment(new Date()).format(DATE_FORMAT)) {
                return ngayHachToanData;
            } else {
                return now;
            }
        } else {
            return now;
        }
    }

    /*
    * Hautv
    * */
    private getDate(date: Moment) {
        const result = date != null && date.isValid() ? date.format(DATE_FORMAT) : null;
        return result;
    }

    /*
    * @Author hieugie
    *
    * check 2 object có bằng nhau hay k
    *
    *
    * */
    isEquivalent(a, b) {
        // Create arrays of property names
        if (!a || !b) {
            return false;
        }
        const aProps = Object.getOwnPropertyNames(a);
        const bProps = Object.getOwnPropertyNames(b);

        // If number of properties is different,
        // objects are not equivalent
        if (aProps.length !== bProps.length) {
            // console.log('a.length: ' + aProps.length);
            // console.log('a: ' + aProps);
            // console.log('b.length: ' + bProps.length);
            // console.log('b: ' + bProps);
            return false;
        }

        for (let i = 0; i < aProps.length; i++) {
            const propName = aProps[i];

            // Trường hợp 2 trường cần so sánh là ngày tháng
            if (a[propName] instanceof moment && b[propName] instanceof moment) {
                if (!a[propName].format(DATE_FORMAT) === a[propName].format(DATE_FORMAT)) {
                    return false;
                }
                continue;
            }

            // Trường hợp 2 trường cần so sánh là object
            if (a[propName] instanceof Object && b[propName] instanceof Object) {
                if (!this.isEquivalent(a[propName], b[propName])) {
                    return false;
                }
                continue;
            }

            // If values of same property are not equal,
            // objects are not equivalent
            if (a[propName] !== b[propName]) {
                console.log('a: ' + aProps);
                console.log('b: ' + bProps);
                console.log('a.value: ' + a[propName]);
                console.log('b.value: ' + b[propName]);
                console.log('index: ' + i);
                console.log('name: ' + propName);
                return false;
            }
        }

        // If we made it this far, objects
        // are considered equivalent
        return true;
    }

    isEquivalentArray(a, b) {
        if ((!a && b) || (a && !b)) {
            return false;
        }
        if (!a && !b) {
            return true;
        }
        if (a && b && a.length !== b.length) {
            return false;
        }

        for (let i = 0; i < a.length; i++) {
            if (!this.isEquivalent(a[i], b[i])) {
                return false;
            }
        }

        return true;
    }

    getListColumnsAccountDebitAndCredit(): string[] {
        return ['accountNumber', this.translate.instant('ebwebApp.comboBox.accountDebit.accountNameCol')];
    }

    getHeaderListColumnsAccountDebitAndCredit(): string[] {
        return [
            this.translate.instant('ebwebApp.comboBox.accountDebit.accountNumber'),
            this.translate.instant('ebwebApp.comboBox.accountDebit.accountName')
        ];
    }

    getListHeaderColumnsAccountingObject(): string[] {
        return [
            this.translate.instant('ebwebApp.comboBox.accountingObjects.accountingObjectCode'),
            this.translate.instant('ebwebApp.comboBox.accountingObjects.accountingObjectName'),
            this.translate.instant('ebwebApp.comboBox.accountingObjects.accountingObjectAddress')
        ];
    }

    getListHeaderColumnsEmployees(): string[] {
        return [
            this.translate.instant('ebwebApp.comboBox.employees.accountingObjectCode'),
            this.translate.instant('ebwebApp.comboBox.employees.accountingObjectName')
        ];
    }

    getListHeaderColumnsCurrency(): string[] {
        return [
            this.translate.instant('ebwebApp.comboBox.currency.currencyCode'),
            this.translate.instant('ebwebApp.comboBox.currency.currencyName')
        ];
    }

    getListHeaderMaterialGoodsForPPservices(): string[] {
        return [
            this.translate.instant('ebwebApp.comboBox.materialGoods.materialGoodsCode'),
            this.translate.instant('ebwebApp.comboBox.materialGoods.materialGoodsName')
        ];
    }

    getListHeaderColumnsUnit(): string[] {
        return [this.translate.instant('ebwebApp.comboBox.unit.unitName')];
    }

    getListHeaderValueNumber(): String[] {
        return [this.translate.instant('ebwebApp.comboBox.unit.valueNumber')];
    }

    /*
    * @author anmt
    * check định dạng đúng Nobook và độ dài
    * return true or false
    * */
    checkNoBook(noFBook: string, account?: any): boolean {
        const regExp = /\d/;
        if (noFBook.length > 25) {
            this.toastr.error(
                this.translate.instant('ebwebApp.mBTellerPaper.error.noLengthGreater25'),
                this.translate.instant('ebwebApp.mBTellerPaper.error.error')
            );
            return false;
        } else if (!regExp.test(noFBook)) {
            this.toastr.error(
                this.translate.instant('ebwebApp.mBTellerPaper.error.noExistNumber'),
                this.translate.instant('ebwebApp.mBTellerPaper.error.error')
            );
            return false;
        }
        return true;
        /*const regExp = /^\D*\d{3,}.*$/;
        if (account) {
            const typeGenVocherNo = account.systemOption.find(x => x.code === TangSoChungTu && x.data).data;
            if (typeGenVocherNo === '1') {
                const no = Array.from(noFBook)
                    .reverse()
                    .join('');
                if (no.length > 25) {
                    this.toastr.error(
                        this.translate.instant('ebwebApp.mBTellerPaper.error.noLengthGreater25'),
                        this.translate.instant('ebwebApp.mBTellerPaper.error.error')
                    );
                    return false;
                } else if (!regExp.test(no)) {
                    this.toastr.error(
                        this.translate.instant('ebwebApp.mBTellerPaper.error.noValidateDefine'),
                        this.translate.instant('ebwebApp.mBTellerPaper.error.error')
                    );
                    return false;
                } else {
                    return true;
                }
            } else {
                if (noFBook.length > 25) {
                    this.toastr.error(
                        this.translate.instant('ebwebApp.mBTellerPaper.error.noLengthGreater25'),
                        this.translate.instant('ebwebApp.mBTellerPaper.error.error')
                    );
                    return false;
                } else if (!regExp.test(noFBook)) {
                    this.toastr.error(
                        this.translate.instant('ebwebApp.mBTellerPaper.error.noValidateDefine'),
                        this.translate.instant('ebwebApp.mBTellerPaper.error.error')
                    );
                    return false;
                } else {
                    return true;
                }
            }
        } else {
            if (noFBook.length > 25) {
                this.toastr.error(
                    this.translate.instant('ebwebApp.mBTellerPaper.error.noLengthGreater25'),
                    this.translate.instant('ebwebApp.mBTellerPaper.error.error')
                );
                return false;
            } else if (!regExp.test(noFBook)) {
                this.toastr.error(
                    this.translate.instant('ebwebApp.mBTellerPaper.error.noValidateDefine'),
                    this.translate.instant('ebwebApp.mBTellerPaper.error.error')
                );
                return false;
            } else {
                return true;
            }
        }*/
    }

    /*
    * @author Hautv
    * check Accout With DetailType
    * trường nào không cần check thì truyền [] = null
    * return null : Các trường bắt buộc hợp lệ
    * return !== null : Các trường bắt buộc không hợp lệ
    * Giá trị trả về là câu thông báo string
    * */
    checkAccoutWithDetailType(
        debitAccounts: IAccountList[],
        creditAccounts: IAccountList[],
        listDetail: object[],
        accountingObjects: IAccountingObject[],
        costSets: ICostSet[],
        eMContracts: IEMContract[],
        materialGoods: IMaterialGoods[],
        bankAccountDetails: IBankAccountDetails[],
        departments: IOrganizationUnit[],
        expenseItems: IExpenseItem[],
        budgetItems: IBudgetItem[],
        statisticsCodes: IStatisticsCode[],
        voucher?: object,
        option?: boolean
    ) {
        let result = null;
        const tkNo = 'debitAccount';
        const tkCo = 'creditAccount';
        const dtT = 'detailType';
        let continueForEach = true;
        listDetail.forEach((dt: object) => {
            if (!option) {
                if (continueForEach) {
                    if (dt.hasOwnProperty(tkNo)) {
                        const acc_f = debitAccounts.find(n => n.accountNumber === dt[tkNo]);
                        result = this.getResult(
                            acc_f,
                            dt,
                            accountingObjects,
                            costSets,
                            eMContracts,
                            materialGoods,
                            bankAccountDetails,
                            departments,
                            expenseItems,
                            budgetItems,
                            statisticsCodes,
                            voucher
                        );
                        if (result) {
                            continueForEach = false;
                        }
                    }
                }
                if (continueForEach) {
                    if (dt.hasOwnProperty(tkCo)) {
                        const acc_f = creditAccounts.find(n => n.accountNumber === dt[tkCo]);
                        result = this.getResult(
                            acc_f,
                            dt,
                            accountingObjects,
                            costSets,
                            eMContracts,
                            materialGoods,
                            bankAccountDetails,
                            departments,
                            expenseItems,
                            budgetItems,
                            statisticsCodes,
                            voucher
                        );
                        if (result) {
                            continueForEach = false;
                        }
                    }
                }
            } else {
                if (continueForEach) {
                    if (dt.hasOwnProperty(tkNo)) {
                        const acc_f = debitAccounts.find(n => n.accountNumber === dt[tkNo]);
                        result = this.getResult(
                            acc_f,
                            dt,
                            accountingObjects,
                            costSets,
                            eMContracts,
                            materialGoods,
                            bankAccountDetails,
                            departments,
                            expenseItems,
                            budgetItems,
                            statisticsCodes,
                            voucher,
                            1
                        );
                        if (result) {
                            continueForEach = false;
                        }
                    }
                }
                if (continueForEach) {
                    if (dt.hasOwnProperty(tkCo)) {
                        const acc_f = creditAccounts.find(n => n.accountNumber === dt[tkCo]);
                        result = this.getResult(
                            acc_f,
                            dt,
                            accountingObjects,
                            costSets,
                            eMContracts,
                            materialGoods,
                            bankAccountDetails,
                            departments,
                            expenseItems,
                            budgetItems,
                            statisticsCodes,
                            voucher,
                            2
                        );
                        if (result) {
                            continueForEach = false;
                        }
                    }
                }
            }
        });
        return result;
    }

    /*@author Hautv*/
    private getResult(
        acc_f: IAccountList,
        dt: object,
        accountingObjects: IAccountingObject[],
        costSets: ICostSet[],
        eMContracts: IEMContract[],
        materialGoods: IMaterialGoods[],
        bankAccountDetails: IBankAccountDetails[],
        departments: IOrganizationUnit[],
        expenseItems: IExpenseItem[],
        budgetItems: IBudgetItem[],
        statisticsCodes: IStatisticsCode[],
        voucher?: object,
        accType?: number
    ) {
        let result = null;
        if (acc_f) {
            if (acc_f.detailType) {
                const lstacc_Dtt = acc_f.detailType.split(';');
                lstacc_Dtt.forEach((detailType: string) => {
                    if (detailType === '0') {
                        if (accountingObjects) {
                            if (accType === 1) {
                                const acc_Object = accountingObjects.find(n => n.id === dt['debitAccountingObjectID']);
                                if (!acc_Object) {
                                    result =
                                        'Đối tượng bị trống!\n' +
                                        'Tài khoản ' +
                                        acc_f.accountNumber +
                                        ' chi tiết theo Đối tượng nợ Nhà cung cấp';
                                } else if (acc_Object.objectType !== 0 && acc_Object.objectType !== 2) {
                                    result =
                                        'Đối tượng bị trống!\n' +
                                        'Tài khoản ' +
                                        acc_f.accountNumber +
                                        ' chi tiết theo Đối tượng nợ Nhà cung cấp';
                                }
                            } else if (accType === 2) {
                                const acc_Object = accountingObjects.find(n => n.id === dt['creditAccountingObjectID']);
                                if (!acc_Object) {
                                    result =
                                        'Đối tượng bị trống!\n' +
                                        'Tài khoản ' +
                                        acc_f.accountNumber +
                                        ' chi tiết theo Đối tượng có Nhà cung cấp';
                                } else if (acc_Object.objectType !== 0 && acc_Object.objectType !== 2) {
                                    result =
                                        'Đối tượng bị trống!\n' +
                                        'Tài khoản ' +
                                        acc_f.accountNumber +
                                        ' chi tiết theo Đối tượng có Nhà cung cấp';
                                }
                            } else {
                                if (dt.hasOwnProperty('accountingObject')) {
                                    if (!dt['accountingObject']) {
                                        result =
                                            'Đối tượng bị trống!\n' +
                                            'Tài khoản ' +
                                            acc_f.accountNumber +
                                            ' chi tiết theo Đối tượng Nhà cung cấp';
                                    } else {
                                        const acc_Object = accountingObjects.find(n => n.id === dt['accountingObject']['id']);
                                        if (!acc_Object) {
                                            result =
                                                'Đối tượng bị trống!\n' +
                                                'Tài khoản ' +
                                                acc_f.accountNumber +
                                                ' chi tiết theo Đối tượng Nhà cung cấp';
                                        } else if (acc_Object.objectType !== 0 && acc_Object.objectType !== 2) {
                                            result =
                                                'Đối tượng bị trống!\n' +
                                                'Tài khoản ' +
                                                acc_f.accountNumber +
                                                ' chi tiết theo Đối tượng Nhà cung cấp';
                                        }
                                    }
                                } else {
                                    const acc_Object = accountingObjects.find(
                                        n => n.id === dt['accountingObjectID'] || n.id === dt['accountingObjectId']
                                    );
                                    if (!acc_Object) {
                                        result =
                                            'Đối tượng bị trống!\n' +
                                            'Tài khoản ' +
                                            acc_f.accountNumber +
                                            ' chi tiết theo Đối tượng Nhà cung cấp';
                                    } else if (acc_Object.objectType !== 0 && acc_Object.objectType !== 2) {
                                        result =
                                            'Đối tượng bị trống!\n' +
                                            'Tài khoản ' +
                                            acc_f.accountNumber +
                                            ' chi tiết theo Đối tượng Nhà cung cấp';
                                    }
                                }
                            }
                        }
                    } else if (detailType === '1') {
                        if (accountingObjects) {
                            if (accType === 1) {
                                const acc_Object = accountingObjects.find(n => n.id === dt['debitAccountingObjectID']);
                                if (!acc_Object) {
                                    result =
                                        'Đối tượng bị trống!\n' +
                                        'Tài khoản ' +
                                        acc_f.accountNumber +
                                        ' chi tiết theo Đối tượng nợ Khách hàng';
                                } else if (acc_Object.objectType !== 1 && acc_Object.objectType !== 2) {
                                    result =
                                        'Đối tượng bị trống!\n' +
                                        'Tài khoản ' +
                                        acc_f.accountNumber +
                                        ' chi tiết theo Đối tượng nợ Khách hàng';
                                }
                            } else if (accType === 2) {
                                const acc_Object = accountingObjects.find(n => n.id === dt['creditAccountingObjectID']);
                                if (!acc_Object) {
                                    result =
                                        'Đối tượng bị trống!\n' +
                                        'Tài khoản ' +
                                        acc_f.accountNumber +
                                        ' chi tiết theo Đối tượng có Khách hàng';
                                } else if (acc_Object.objectType !== 1 && acc_Object.objectType !== 2) {
                                    result =
                                        'Đối tượng bị trống!\n' +
                                        'Tài khoản ' +
                                        acc_f.accountNumber +
                                        ' chi tiết theo Đối tượng có Khách hàng';
                                }
                            } else {
                                if (dt.hasOwnProperty('accountingObject')) {
                                    if (!dt['accountingObject']) {
                                        result =
                                            'Đối tượng bị trống!\n' +
                                            'Tài khoản ' +
                                            acc_f.accountNumber +
                                            ' chi tiết theo Đối tượng Khách hàng';
                                    } else {
                                        const acc_Object = accountingObjects.find(n => n.id === dt['accountingObject']['id']);
                                        if (!acc_Object) {
                                            result =
                                                'Đối tượng bị trống!\n' +
                                                'Tài khoản ' +
                                                acc_f.accountNumber +
                                                ' chi tiết theo Đối tượng Khách hàng';
                                        } else if (acc_Object.objectType !== 1 && acc_Object.objectType !== 2) {
                                            result =
                                                'Đối tượng bị trống!\n' +
                                                'Tài khoản ' +
                                                acc_f.accountNumber +
                                                ' chi tiết theo Đối tượng Khách hàng';
                                        }
                                    }
                                } else {
                                    const acc_Object = accountingObjects.find(
                                        n => n.id === dt['accountingObjectID'] || n.id === dt['accountingObjectId']
                                    );
                                    if (!acc_Object) {
                                        result =
                                            'Đối tượng bị trống!\n' +
                                            'Tài khoản ' +
                                            acc_f.accountNumber +
                                            ' chi tiết theo Đối tượng Khách hàng';
                                    } else if (acc_Object.objectType !== 1 && acc_Object.objectType !== 2) {
                                        result =
                                            'Đối tượng bị trống!\n' +
                                            'Tài khoản ' +
                                            acc_f.accountNumber +
                                            ' chi tiết theo Đối tượng Khách hàng';
                                    }
                                }
                            }
                        }
                    } else if (detailType === '2') {
                        if (accountingObjects) {
                            if (accType === 1) {
                                const acc_Object = accountingObjects.find(n => n.id === dt['debitAccountingObjectID']);
                                if (!acc_Object) {
                                    result =
                                        'Đối tượng bị trống!\n' +
                                        'Tài khoản ' +
                                        acc_f.accountNumber +
                                        ' chi tiết theo Đối tượng nợ Nhân viên';
                                } else if (!acc_Object.isEmployee) {
                                    result =
                                        'Đối tượng bị trống!\n' +
                                        'Tài khoản ' +
                                        acc_f.accountNumber +
                                        ' chi tiết theo Đối tượng nợ Nhân viên';
                                }
                            } else if (accType === 2) {
                                const acc_Object = accountingObjects.find(n => n.id === dt['creditAccountingObjectID']);
                                if (!acc_Object) {
                                    result =
                                        'Đối tượng bị trống!\n' +
                                        'Tài khoản ' +
                                        acc_f.accountNumber +
                                        ' chi tiết theo Đối tượng có Nhân viên';
                                } else if (!acc_Object.isEmployee) {
                                    result =
                                        'Đối tượng bị trống!\n' +
                                        'Tài khoản ' +
                                        acc_f.accountNumber +
                                        ' chi tiết theo Đối tượng có Nhân viên';
                                }
                            } else {
                                if (dt.hasOwnProperty('accountingObject')) {
                                    if (!dt['accountingObject']) {
                                        result =
                                            'Đối tượng bị trống!\n' +
                                            'Tài khoản ' +
                                            acc_f.accountNumber +
                                            ' chi tiết theo Đối tượng Nhân viên';
                                    } else {
                                        const acc_Object = accountingObjects.find(n => n.id === dt['accountingObject']['id']);
                                        if (!acc_Object) {
                                            result =
                                                'Đối tượng bị trống!\n' +
                                                'Tài khoản ' +
                                                acc_f.accountNumber +
                                                ' chi tiết theo Đối tượng Nhân viên';
                                        } else if (!acc_Object.isEmployee) {
                                            result =
                                                'Đối tượng bị trống!\n' +
                                                'Tài khoản ' +
                                                acc_f.accountNumber +
                                                ' chi tiết theo Đối tượng Nhân viên';
                                        }
                                    }
                                } else {
                                    const acc_Object = accountingObjects.find(
                                        n => n.id === dt['accountingObjectID'] || n.id === dt['accountingObjectId']
                                    );
                                    if (!acc_Object) {
                                        result =
                                            'Đối tượng bị trống!\n' +
                                            'Tài khoản ' +
                                            acc_f.accountNumber +
                                            ' chi tiết theo Đối tượng Nhân viên';
                                    } else if (!acc_Object.isEmployee) {
                                        result =
                                            'Đối tượng bị trống!\n' +
                                            'Tài khoản ' +
                                            acc_f.accountNumber +
                                            ' chi tiết theo Đối tượng Nhân viên';
                                    }
                                }
                            }
                        }
                    } else if (detailType === '3') {
                        if (costSets) {
                            if (dt.hasOwnProperty('costSet')) {
                                if (!dt['costSet']) {
                                    result =
                                        'Đối tượng tập hợp chi phí bị trống!\n' +
                                        'Tài khoản ' +
                                        acc_f.accountNumber +
                                        ' chi tiết theo đối tượng tập hợp chi phí';
                                } else {
                                    const costSet = costSets.find(n => n.id === dt['costSet']['id']);
                                    if (!costSet) {
                                        result =
                                            'Đối tượng tập hợp chi phí bị trống!\n' +
                                            'Tài khoản ' +
                                            acc_f.accountNumber +
                                            ' chi tiết theo đối tượng tập hợp chi phí';
                                    }
                                }
                            } else {
                                const costSet = costSets.find(n => n.id === dt['costSetID']);
                                if (!costSet) {
                                    result =
                                        'Đối tượng tập hợp chi phí bị trống!\n' +
                                        'Tài khoản ' +
                                        acc_f.accountNumber +
                                        ' chi tiết theo đối tượng tập hợp chi phí';
                                }
                            }
                        }
                    } else if (detailType === '4') {
                        if (eMContracts) {
                            if (dt.hasOwnProperty('eMContract')) {
                                if (!dt['eMContract']) {
                                    result = 'Hợp đồng bị trống!\n' + 'Tài khoản ' + acc_f.accountNumber + ' chi tiết theo hợp đồng';
                                } else {
                                    const eMContract = eMContracts.find(n => n.id === dt['eMContract']['id']);
                                    if (!eMContract) {
                                        result = 'Hợp đồng bị trống!\n' + 'Tài khoản ' + acc_f.accountNumber + ' chi tiết theo hợp đồng';
                                    }
                                }
                            } else {
                                const eMContract = eMContracts.find(n => n.id === dt['contractID']);
                                if (!eMContract) {
                                    result = 'Hợp đồng bị trống!\n' + 'Tài khoản ' + acc_f.accountNumber + ' chi tiết theo hợp đồng';
                                }
                            }
                        }
                    } else if (detailType === '5') {
                        if (materialGoods) {
                            if (dt.hasOwnProperty('materialGood')) {
                                if (!dt['materialGood']) {
                                    result =
                                        'Vật tư hàng hóa công cụ dụng cụ bị trống!\n' +
                                        'Tài khoản ' +
                                        acc_f.accountNumber +
                                        ' chi tiết theo vật tư hàng hóa công cụ dụng cụ';
                                } else {
                                    const materialGood = materialGoods.find(n => n.id === dt['materialGood']['id']);
                                    if (!materialGood) {
                                        result =
                                            'Vật tư hàng hóa công cụ dụng cụ bị trống!\n' +
                                            'Tài khoản ' +
                                            acc_f.accountNumber +
                                            ' chi tiết theo vật tư hàng hóa công cụ dụng cụ';
                                    }
                                }
                            } else if (dt.hasOwnProperty('materialGoods')) {
                                if (!dt['materialGoods']) {
                                    result =
                                        'Vật tư hàng hóa công cụ dụng cụ bị trống!\n' +
                                        'Tài khoản ' +
                                        acc_f.accountNumber +
                                        ' chi tiết theo vật tư hàng hóa công cụ dụng cụ';
                                } else {
                                    const materialGood = materialGoods.find(n => n.id === dt['materialGoods']['id']);
                                    if (!materialGood) {
                                        result =
                                            'Vật tư hàng hóa công cụ dụng cụ bị trống!\n' +
                                            'Tài khoản ' +
                                            acc_f.accountNumber +
                                            ' chi tiết theo vật tư hàng hóa công cụ dụng cụ';
                                    }
                                }
                            } else {
                                const materialGood = materialGoods.find(n => n.id === dt['materialGoodID']);
                                if (!materialGood) {
                                    result =
                                        'Vật tư hàng hóa công cụ dụng cụ bị trống!\n' +
                                        'Tài khoản ' +
                                        acc_f.accountNumber +
                                        ' chi tiết theo vật tư hàng hóa công cụ dụng cụ';
                                }
                            }
                        }
                    } else if (detailType === '6') {
                        if (bankAccountDetails) {
                            if (dt.hasOwnProperty('bankAccountDetails')) {
                                if (!dt['bankAccountDetails']) {
                                    result =
                                        'Tài khoản ngân hàng bị trống!\n' +
                                        'Tài khoản ' +
                                        acc_f.accountNumber +
                                        ' chi tiết theo tài khoản ngân hàng';
                                } else {
                                    const bankAccountDetail = bankAccountDetails.find(n => n.id === dt['bankAccountDetails']['id']);
                                    if (!bankAccountDetail) {
                                        result =
                                            'Tài khoản ngân hàng bị trống!\n' +
                                            'Tài khoản ' +
                                            acc_f.accountNumber +
                                            ' chi tiết theo tài khoản ngân hàng';
                                    }
                                }
                            } else if (voucher) {
                                const bankAccountDetail = bankAccountDetails.find(n => n.id === voucher['bankAccountDetailID']);
                                if (!bankAccountDetail) {
                                    result =
                                        'Tài khoản ngân hàng bị trống!\n' +
                                        'Tài khoản ' +
                                        acc_f.accountNumber +
                                        ' chi tiết theo tài khoản ngân hàng';
                                }
                            } else {
                                const bankAccountDetail = bankAccountDetails.find(n => n.id === dt['bankAccountDetailID']);
                                if (!bankAccountDetail) {
                                    result =
                                        'Tài khoản ngân hàng bị trống!\n' +
                                        'Tài khoản ' +
                                        acc_f.accountNumber +
                                        ' chi tiết theo tài khoản ngân hàng';
                                }
                            }
                        }
                    } else if (detailType === '7') {
                        if (departments) {
                            if (dt.hasOwnProperty('organizationUnit')) {
                                if (!dt['organizationUnit']) {
                                    result = 'Phòng ban bị trống!\n' + 'Tài khoản ' + acc_f.accountNumber + ' chi tiết theo phòng ban';
                                } else {
                                    const organizationUnit = departments.find(n => n.id === dt['organizationUnit']['id']);
                                    if (!organizationUnit) {
                                        result = 'Phòng ban bị trống!\n' + 'Tài khoản ' + acc_f.accountNumber + ' chi tiết theo phòng ban';
                                    }
                                }
                            } else if (dt.hasOwnProperty('department')) {
                                if (!dt['department']) {
                                    result = 'Phòng ban bị trống!\n' + 'Tài khoản ' + acc_f.accountNumber + ' chi tiết theo phòng ban';
                                } else {
                                    const department = departments.find(n => n.id === dt['department']['id']);
                                    if (!department) {
                                        result = 'Phòng ban bị trống!\n' + 'Tài khoản ' + acc_f.accountNumber + ' chi tiết theo phòng ban';
                                    }
                                }
                            } else {
                                const organizationUnit = departments.find(
                                    n => n.id === dt['organizationUnitID'] || n.id === dt['departmentID']
                                );
                                if (!organizationUnit) {
                                    result = 'Phòng ban bị trống!\n' + 'Tài khoản ' + acc_f.accountNumber + ' chi tiết theo phòng ban';
                                }
                            }
                        }
                    } else if (detailType === '8') {
                        if (expenseItems) {
                            if (dt.hasOwnProperty('expenseItem')) {
                                if (!dt['expenseItem']) {
                                    result =
                                        'Khoản mục chi phí bị trống!\n' +
                                        'Tài khoản ' +
                                        acc_f.accountNumber +
                                        ' chi tiết theo khoản mục chi phí';
                                } else {
                                    const expenseItem = expenseItems.find(n => n.id === dt['expenseItem']['id']);
                                    if (!expenseItem) {
                                        result =
                                            'Khoản mục chi phí bị trống!\n' +
                                            'Tài khoản ' +
                                            acc_f.accountNumber +
                                            ' chi tiết theo khoản mục chi phí';
                                    }
                                }
                            } else {
                                const expenseItem = expenseItems.find(n => n.id === dt['expenseItemID']);
                                if (!expenseItem) {
                                    result =
                                        'Khoản mục chi phí bị trống!\n' +
                                        'Tài khoản ' +
                                        acc_f.accountNumber +
                                        ' chi tiết theo khoản mục chi phí';
                                }
                            }
                        }
                    } else if (detailType === '9') {
                        if (budgetItems) {
                            if (dt.hasOwnProperty('budgetItem')) {
                                if (!dt['budgetItem']) {
                                    result = 'Mục thu chi bị trống!\n' + 'Tài khoản ' + acc_f.accountNumber + ' chi tiết theo mục thu chi';
                                } else {
                                    const budgetItem = budgetItems.find(n => n.id === dt['budgetItem']['id']);
                                    if (!budgetItem) {
                                        result =
                                            'Mục thu chi bị trống!\n' + 'Tài khoản ' + acc_f.accountNumber + ' chi tiết theo mục thu chi';
                                    }
                                }
                            } else {
                                const budgetItem = budgetItems.find(n => n.id === dt['budgetItemID']);
                                if (!budgetItem) {
                                    result = 'Mục thu chi bị trống!\n' + 'Tài khoản ' + acc_f.accountNumber + ' chi tiết theo mục thu chi';
                                }
                            }
                        }
                    } else if (detailType === '10') {
                        if (statisticsCodes) {
                            if (dt.hasOwnProperty('statisticsCode')) {
                                if (!dt['statisticsCode']) {
                                    result = 'Mã thống kê bị trống!\n' + 'Tài khoản ' + acc_f.accountNumber + ' chi tiết theo mã thống kê';
                                } else {
                                    const statisticsCode = statisticsCodes.find(n => n.id === dt['statisticsCode']['id']);
                                    if (!statisticsCode) {
                                        result =
                                            'Mã thống kê bị trống!\n' + 'Tài khoản ' + acc_f.accountNumber + ' chi tiết theo mã thống kê';
                                    }
                                }
                            } else {
                                const statisticsCode = statisticsCodes.find(n => n.id === dt['statisticsCodeID']);
                                if (!statisticsCode) {
                                    result = 'Mã thống kê trống!\n' + 'Tài khoản ' + acc_f.accountNumber + ' chi tiết theo mã thống kê';
                                }
                            }
                        }
                    }
                    if (result) {
                        return result;
                    }
                });
            }
        }
        return result;
    }

    getListHeaderColumnsBankAccountDetails(): string[] {
        return [
            this.translate.instant('ebwebApp.comboBox.bankAccountDetails.bankAccount'),
            this.translate.instant('ebwebApp.comboBox.bankAccountDetails.bankName')
        ];
    }

    getListHeaderColumnsAccountingObjectBankAccount(): string[] {
        return [
            this.translate.instant('ebwebApp.comboBox.bankAccountDetails.bankAccount'),
            this.translate.instant('ebwebApp.comboBox.bankAccountDetails.bankName')
        ];
    }

    getListHeaderColumnsCreditCard(): string[] {
        return [
            this.translate.instant('ebwebApp.comboBox.creditCard.creditCardNumber'),
            this.translate.instant('ebwebApp.comboBox.creditCard.creditCardType')
        ];
    }

    formatStrDate(dateString: string) {
        const result = moment(dateString, 'DD/MM/YYYY').format('DD/MM/YYYY');
        if (result === 'Invalid date') {
            return '';
        }
        return result;
    }

    /*
    * @author Hautv
    * Kiểm tra trùng số chứng từ
    * return false số chứng từ đã tồn tại
    * */
    checkDuplicateNoVoucher(req?: any): Observable<any> {
        const options = createRequestOption(req);
        return this.http.get(SERVER_API_URL + 'api/voucher/checkDuplicateNoVoucher', {
            params: options,
            observe: 'response'
        });
    }

    /**
     * @author dungvm
     * @param object object cần làm tròn
     * @param systemOption
     * Hàm làm tròn số thập phân cho object
     */
    roundObject(object: any, systemOption: any, isOriginalCurrency?: boolean) {
        for (const property in object) {
            if (object.hasOwnProperty(property)) {
                if (typeof object[property] === 'number') {
                    const value = object[property];
                    const type = this.getTypeFromObjectKey(property.toLowerCase(), isOriginalCurrency);
                    object[property] = this.round(value, systemOption, type);
                } else if (object[property] instanceof Object) {
                    this.roundObject(object[property], systemOption);
                }
            }
        }
        return object;
    }

    roundObjects(objects: any[], systemOption: any, isOriginalCurrency?: boolean) {
        objects.forEach(object => {
            for (const property in object) {
                if (object.hasOwnProperty(property)) {
                    if (typeof object[property] === 'number') {
                        const value = object[property];
                        const type = this.getTypeFromObjectKey(property.toLowerCase(), isOriginalCurrency);
                        object[property] = this.round(value, systemOption, type);
                    } else if (object[property] instanceof Object) {
                        this.roundObject(object[property], systemOption);
                    }
                }
            }
        });
        return objects;
    }

    /**
     * @author dungvm
     * @param property tên thuộc tính
     */
    getTypeFromObjectKey(property: string, isOriginalCurrency?: boolean): number {
        const typeMap = {
            unitPriceOriginal: isOriginalCurrency ? 1 : 2,
            unitPrice: 1,
            quantity: 3,
            totalAmountOriginal: isOriginalCurrency ? 7 : 8,
            totalAmount: 7,
            totalVATAmountOriginal: isOriginalCurrency ? 7 : 8,
            totalVATAmount: 7,
            discountAmountOriginal: isOriginalCurrency ? 7 : 8,
            discountAmount: 7,
            vatAmountOriginal: isOriginalCurrency ? 7 : 8,
            vatAmount: 7,
            vATAmountOriginal: isOriginalCurrency ? 7 : 8,
            vATAmount: 7,
            totalOriginal: isOriginalCurrency ? 7 : 8,
            total: 7,
            amountOriginal: isOriginalCurrency ? 7 : 8,
            amount: 7,
            mainUnitPrice: 5,
            discountRate: 5,
            mainConvertRate: 5,
            exchangeRate: 4,
            exportTaxAmount: 7
        };

        for (const prop in typeMap) {
            if (typeMap.hasOwnProperty(prop) && property.includes(prop.toLowerCase())) {
                return typeMap[prop];
            }
        }
        return 1;
    }

    round(value: number, systemOption: any, type: number): number {
        let precision = 0;
        for (const item of systemOption) {
            if (item.code === this.getStringFromType(type)) {
                precision = parseInt(item.data, 10);
                break;
            }
        }
        return Number(Math.round(parseFloat(`${value}e${precision}`)) + 'e-' + precision);
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

    pad(num: string, size: number): string {
        let s = (num ? num : '') + '';
        while (s.length < size) {
            s = '0' + s;
        }
        return s;
    }

    changeReceivedQuantity(receivedQuantity: number, quantityReceipt: number): boolean {
        if (receivedQuantity > quantityReceipt) {
            this.toastr.error(
                this.translate.instant('global.messages.validate.receivedQuantityLarger'),
                this.translate.instant('ebwebApp.pPInvoice.message')
            );
            return false;
        }
        return true;
    }

    revertUUID(objects: any[]) {
        const propertiesName = [
            'id',
            'unitID',
            'mainUnitID',
            'materialGoodsID',
            'repositoryID',
            'accountingObjectID',
            'saInvoiceID',
            'saInvoiceDetailID',
            'departmentID',
            'expenseItemID',
            'budgetItemID',
            'costSetID',
            'contractID',
            'statisticsCodeID',
            'saBillDetailID',
            'saBillID',
            'careerGroupID'
        ];

        objects.forEach(item => {
            propertiesName.forEach(prop => {
                if (item.hasOwnProperty(prop) && item[prop]) {
                    item[prop] =
                        item[prop].substring(6, 8) +
                        item[prop].substring(4, 6) +
                        item[prop].substring(2, 4) +
                        item[prop].substring(0, 2) +
                        '-' +
                        item[prop].substring(11, 13) +
                        item[prop].substring(9, 11) +
                        '-' +
                        item[prop].substring(16, 18) +
                        item[prop].substring(14, 16) +
                        item[prop].substring(18);
                }
            });
        });
    }

    roundObjectWithAccount(object: any, account: any, currencyCode: string) {
        for (const property in object) {
            if (object.hasOwnProperty(property)) {
                if (typeof object[property] === 'number') {
                    const value = object[property];
                    const type = this.getTypeFromObjectKeyWithType(property.toLowerCase(), account, currencyCode);
                    object[property] = this.round(value, account.systemOption, type);
                } else if (object[property] instanceof Object) {
                    this.roundObject(object[property], account.systemOption);
                }
            }
        }
        return object;
    }

    roundObjectsWithAccount(objects: any[], account: any, currencyCode: string) {
        objects.forEach(object => {
            for (const property in object) {
                if (object.hasOwnProperty(property)) {
                    if (typeof object[property] === 'number') {
                        const value = object[property];
                        const type = this.getTypeFromObjectKeyWithType(property.toLowerCase(), account, currencyCode);
                        object[property] = this.round(value, account.systemOption, type);
                    } else if (object[property] instanceof Object) {
                        this.roundObject(object[property], account.systemOption);
                    }
                }
            }
        });
        return objects;
    }

    /**
     * @author congnd
     * @param property tên thuộc tính
     * @param account
     * @param currencyCode
     */
    getTypeFromObjectKeyWithType(property: string, account: any, currencyCode: string): number {
        const typeAmountOriginal = currencyCode === account.organizationUnit.currencyID;
        const typeMap = {
            unitPriceOriginal: typeAmountOriginal ? 1 : 2,
            unitPrice: 1,
            quantity: 3,
            totalAmountOriginal: typeAmountOriginal ? 7 : 8,
            totalAmount: 7,
            totalVATAmountOriginal: typeAmountOriginal ? 7 : 8,
            totalVATAmount: 7,
            discountAmountOriginal: typeAmountOriginal ? 7 : 8,
            discountAmount: 7,
            vatAmountOriginal: typeAmountOriginal ? 7 : 8,
            vatAmount: 7,
            vATAmountOriginal: typeAmountOriginal ? 7 : 8,
            vATAmount: 7,
            totalOriginal: typeAmountOriginal ? 7 : 8,
            total: 7,
            amountOriginal: typeAmountOriginal ? 7 : 8,
            amount: 7,
            mainUnitPrice: 1,
            discountRate: 5,
            mainConvertRate: 5,
            exchangeRate: 4,
            exportTaxAmount: 7
        };

        for (const prop in typeMap) {
            if (typeMap.hasOwnProperty(prop) && property.includes(prop.toLowerCase())) {
                return typeMap[prop];
            }
        }
        return 1;
    }

    deepCopy(object) {
        if (object) {
            return cloneDeep(object);
        }
        return null;
    }

    deepCopyObject(object) {
        if (object) {
            const arrayObject = [object];
            return cloneDeep(arrayObject)[0];
        }
        return null;
    }

    checkQuantityExists(mgForPPOder: any[], details: any[]) {
        const mgForPPOderMap = new Map();
        let textCode: any = '';
        for (let i = 0; i < details.length; i++) {
            const item = mgForPPOder.find(x => x.id === details[i].materialGoodsID && x.materialGoodsInStock < 0);
            if (item) {
                if (mgForPPOderMap.size === 0 || mgForPPOderMap.get(item.materialGoodsCode) !== item.materialGoodsCode) {
                    mgForPPOderMap.set(item.materialGoodsCode, item.materialGoodsCode);
                    textCode += item.materialGoodsCode + ', ';
                }
            }
        }
        return textCode.slice(0, textCode.length - 2);
    }

    checkQuantityExistsTest(mgForPPOder: any[], details: any[]) {
        const mgForPPOderMap = new Map();
        const mgForPPOderMap2 = new Map();
        const mgForPPOderMinimumStockMap = new Map();
        const mgForPPOderInStockMap = new Map();
        let textCode: any = '';
        let textCode2: any = '';
        for (let i = 0; i < details.length; i++) {
            let item = null;
            if (details[i].materialGoodsID) {
                item = mgForPPOder.find(x => x.id === details[i].materialGoodsID);
            } else {
                item = mgForPPOder.find(x => x.id === details[i].materialGood.id);
            }

            if (item && item.materialGoodsType !== 2 && item.materialGoodsType !== 4) {
                // if (item.materialGoodsInStock < 0) {
                //     if (mgForPPOderMap.size === 0 || mgForPPOderMap.get(item.materialGoodsCode) !== item.materialGoodsCode) {
                //         mgForPPOderMap.set(item.materialGoodsCode, item.materialGoodsCode);
                //         textCode += item.materialGoodsCode + ', ';
                //     }
                // }
                if (mgForPPOderMap.size === 0) {
                    mgForPPOderMap.set(item.materialGoodsCode, details[i].quantity);
                }
                if (!mgForPPOderMap.has(item.materialGoodsCode)) {
                    mgForPPOderMap.set(item.materialGoodsCode, details[i].quantity);
                }
                if (item.materialGoodsInStock - Number(mgForPPOderMap.get(item.materialGoodsCode)) < 0) {
                    mgForPPOderInStockMap.set(item.materialGoodsCode, item.materialGoodsCode);
                } else {
                    mgForPPOderMap.set(item.materialGoodsCode, mgForPPOderMap.get(item.materialGoodsCode) + details[i].quantity);
                }
                if (item.minimumStock && item.minimumStock !== 0) {
                    if (mgForPPOderMap2.size === 0) {
                        mgForPPOderMap2.set(item.materialGoodsCode, details[i].quantity);
                    }
                    if (!mgForPPOderMap2.has(item.materialGoodsCode)) {
                        mgForPPOderMap2.set(item.materialGoodsCode, details[i].quantity);
                    }
                    if (item.materialGoodsInStock - Number(mgForPPOderMap2.get(item.materialGoodsCode)) < item.minimumStock) {
                        mgForPPOderMinimumStockMap.set(item.materialGoodsCode, item.materialGoodsCode);
                    } else {
                        mgForPPOderMap2.set(item.materialGoodsCode, mgForPPOderMap2.get(item.materialGoodsCode) + details[i].quantity);
                    }
                }
            }
        }
        for (const value of Array.from(mgForPPOderMinimumStockMap.values())) {
            textCode2 += value + ', ';
        }
        for (const value of Array.from(mgForPPOderInStockMap.values())) {
            textCode += value + ', ';
        }
        return {
            quantityExists: textCode.slice(0, textCode.length - 2),
            minimumStock: textCode2.slice(0, textCode2.length - 2)
        };
    }

    /*
    * @Author Hautv
    * return : Dùng để link đến chứng từ gốc
    * */
    viewVoucherByIDAndTypeGroupID(voucher: VoucherRefCatalogDTO) {
        // this.activeModal.dismiss(true);
        let url = '';
        console.log(voucher.typeGroupID);
        switch (voucher.typeGroupID) {
            // Hàng bán trả lại
            case 33:
                url = `/#/hang-ban/tra-lai/${voucher.id}/edit/from-ref`;
                break;
            // Giảm giá hàng bán
            case 34:
                url = `/#/hang-ban/giam-gia/${voucher.id}/edit/from-ref`;
                break;
            // Xuất hóa đơn
            case 35:
                url = `/#/xuat-hoa-don/${voucher.id}/edit/from-ref`;
                break;
            case 22:
                url = `/#/hang-mua/tra-lai/${voucher.id}/view`;
                break;
            case 23:
                url = `/#/hang-mua/giam-gia/${voucher.id}/view`;
                break;
            case 10:
                url = `/#/mc-receipt/${voucher.id}/edit/from-ref`;
                break;
            case 16:
                url = `/#/mb-deposit/${voucher.id}/edit/from-ref`;
                break;
            case 17:
                url = `/#/mb-credit-card/${voucher.id}/edit/from-ref`;
                break;
            case 70:
                url = `/#/g-other-voucher/${voucher.id}/edit/from-ref`;
                break;
            case 11:
                url = `/#/mc-payment/${voucher.id}/edit/from-ref`;
                break;
            case 31:
                url = `/#/sa-order/${voucher.id}/edit/from-ref`;
                break;
            case 24:
                url = `/#/mua-dich-vu/${voucher.id}/edit/from-ref`;
                break;
            case 40:
                url = `/#/nhap-kho/${voucher.id}/edit/from-ref`;
                break;
            case 20:
                url = `/#/don-mua-hang/${voucher.id}/edit/from-ref`;
                break;
            case 41:
                url = `/#/xuat-kho/${voucher.id}/edit/from-ref`;
                break;
            case 42:
                url = `/#/chuyen-kho/${voucher.id}/edit/from-ref`;
                break;
            case 21:
                this.viewVoucherService.checkViaStockPPInvoice({ id: voucher.id }).subscribe(
                    (res: HttpResponse<any>) => {
                        if (res.body.message === UpdateDataMessages.STOCK_TRUE) {
                            url = `/#/mua-hang/qua-kho-ref/${voucher.id}/edit/1`;
                            window.open(url, '_blank');
                        } else if (res.body.message === UpdateDataMessages.STOCK_FALSE) {
                            url = `/#/mua-hang/khong-qua-kho-ref/${voucher.id}/edit/1`;
                            window.open(url, '_blank');
                        } else {
                            this.toastr.error(this.translate.instant('ebwebApp.pPInvoice.error.default'));
                        }
                        return;
                    },
                    (res: HttpErrorResponse) => {
                        this.toastr.error(this.translate.instant('ebwebApp.pPInvoice.error.default'));
                    }
                );
                return;
            case 18:
                url = `/#/mc-audit/${voucher.id}/edit/from-ref`;
                break;
            case 32:
                url = `/#/chung-tu-ban-hang/${voucher.id}/edit/from-ref`;
                break;
            case 30:
                url = `/#/sa-quote/${voucher.id}/edit/from-ref`;
                break;
            case 12:
            case 13:
            case 14:
                url = `/#/mb-teller-paper/${voucher.id}/edit/from-ref`;
                break;
        }
        window.open(url, '_blank');
    }

    viewRefVoucher(voucher) {
        let url = '';
        switch (voucher.typeGroupID) {
            // Hàng bán trả lại
            case 33:
                url = `/#/hang-ban/tra-lai/${voucher.refID2}/edit/from-ref`;
                break;
            // Giảm giá hàng bán
            case 34:
                url = `/#/hang-ban/giam-gia/${voucher.refID2}/edit/from-ref`;
                break;
            // Xuất hóa đơn
            case 35:
                url = `/#/xuat-hoa-don/${voucher.refID2}/edit/from-ref`;
                break;
            case 22:
                url = `/#/hang-mua/tra-lai/${voucher.refID2}/from-ref`;
                break;
            case 23:
                url = `/#/hang-mua/giam-gia/${voucher.refID2}/from-ref`;
                break;
            case 10:
                url = `/#/mc-receipt/${voucher.refID2}/edit/from-ref`;
                break;
            case 16:
                url = `/#/mb-deposit/${voucher.refID2}/edit/from-ref`;
                break;
            case 17:
                url = `/#/mb-credit-card/${voucher.refID2}/edit/from-ref`;
                break;
            case 70:
                url = `/#/g-other-voucher/${voucher.refID2}/edit/from-ref`;
                break;
            case 11:
                url = `/#/mc-payment/${voucher.refID2}/edit/from-ref`;
                break;
            case 31:
                url = `/#/sa-order/${voucher.refID2}/edit/from-ref`;
                break;
            case 24:
                url = `/#/mua-dich-vu/${voucher.refID2}/edit/from-ref`;
                break;
            case 40:
                url = `/#/nhap-kho/${voucher.refID2}/edit/from-ref`;
                break;
            case 20:
                url = `/#/don-mua-hang/${voucher.refID2}/edit/from-ref`;
                break;
            case 41:
                url = `/#/xuat-kho/${voucher.refID2}/edit/from-ref`;
                break;
            case 42:
                url = `/#/chuyen-kho/${voucher.refID2}/edit/from-ref`;
                break;
            case 21:
                this.viewVoucherService.checkViaStockPPInvoice({ id: voucher.refID2 }).subscribe(
                    (res: HttpResponse<any>) => {
                        if (res.body.message === UpdateDataMessages.STOCK_TRUE) {
                            url = `/#/mua-hang/qua-kho/${voucher.refID2}/edit/1`;
                            window.open(url, '_blank');
                        } else if (res.body.message === UpdateDataMessages.STOCK_FALSE) {
                            url = `/#/mua-hang/khong-qua-kho/${voucher.refID2}/edit/1`;
                            window.open(url, '_blank');
                        } else {
                            this.toastr.error(this.translate.instant('ebwebApp.pPInvoice.error.default'));
                        }
                        return;
                    },
                    (res: HttpErrorResponse) => {
                        this.toastr.error(this.translate.instant('ebwebApp.pPInvoice.error.default'));
                    }
                );
                return;
            case 18:
                url = `/#/mc-audit/${voucher.refID2}/edit/from-ref`;
                break;
            case 32:
                url = `/#/chung-tu-ban-hang/${voucher.refID2}/edit/from-ref`;
                break;
            case 30:
                url = `/#/sa-quote/${voucher.refID2}/edit/from-ref`;
                break;
            case 12:
            case 13:
            case 14:
                url = `/#/mb-teller-paper/${voucher.refID2}/edit/from-ref`;
                break;
        }
        window.open(url, '_blank');
    }

    /*
    * @Author HuyXoan
    * return : Dùng để link đến chứng từ gốc
    * */
    viewByTypeGroupID(voucher: VoucherRefCatalogDTO) {
        // this.activeModal.dismiss(true);
        let url = '';
        console.log(voucher.typeGroupID);
        switch (voucher.typeGroupID) {
            // Hàng bán trả lại
            case 33:
                url = `/#/hang-ban/tra-lai/${voucher.id}/edit/from-ref`;
                break;
            // Giảm giá hàng bán
            case 34:
                url = `/#/hang-ban/giam-gia/${voucher.id}/edit/from-ref`;
                break;
            // Xuất hóa đơn
            case 35:
                url = `/#/xuat-hoa-don/${voucher.id}/edit/from-ref`;
                break;
            case 22:
                url = `/#/hang-mua/tra-lai/${voucher.id}/view`;
                break;
            case 23:
                url = `/#/hang-mua/giam-gia/${voucher.id}/view`;
                break;
            case 10:
                url = `/#/mc-receipt/${voucher.id}/edit/from-ref`;
                break;
            case 16:
                url = `/#/mb-deposit/${voucher.id}/edit/from-ref`;
                break;
            case 17:
                url = `/#/mb-credit-card/${voucher.id}/edit/from-ref`;
                break;
            case 70:
                url = `/#/g-other-voucher/${voucher.id}/edit/from-ref`;
                break;
            case 11:
                url = `/#/mc-payment/${voucher.id}/edit/from-ref`;
                break;
            case 31:
                url = `/#/sa-order/${voucher.id}/edit/from-ref`;
                break;
            case 24:
                url = `/#/mua-dich-vu/${voucher.id}/edit/from-ref`;
                break;
            case 40:
                url = `/#/nhap-kho/${voucher.id}/edit/from-ref`;
                break;
            case 20:
                url = `/#/don-mua-hang/${voucher.ppOrderId}/edit/from-ref`;
                break;
            case 41:
                url = `/#/xuat-kho/${voucher.id}/edit/from-ref`;
                break;
            case 42:
                url = `/#/chuyen-kho/${voucher.id}/edit/from-ref`;
                break;
            case 21:
                this.viewVoucherService.checkViaStockPPInvoice({ id: voucher.id }).subscribe(
                    (res: HttpResponse<any>) => {
                        if (res.body.message === UpdateDataMessages.STOCK_TRUE) {
                            url = `/#/mua-hang/qua-kho-ref/${voucher.id}/edit/1`;
                            window.open(url, '_blank');
                        } else if (res.body.message === UpdateDataMessages.STOCK_FALSE) {
                            url = `/#/mua-hang/khong-qua-kho-ref/${voucher.id}/edit/1`;
                            window.open(url, '_blank');
                        } else {
                            this.toastr.error(this.translate.instant('ebwebApp.pPInvoice.error.default'));
                        }
                        return;
                    },
                    (res: HttpErrorResponse) => {
                        this.toastr.error(this.translate.instant('ebwebApp.pPInvoice.error.default'));
                    }
                );
                return;
            case 18:
                url = `/#/mc-audit/${voucher.id}/edit/from-ref`;
                break;
            case 32:
                url = `/#/chung-tu-ban-hang/${voucher.id}/edit/from-ref`;
                break;
            case 30:
                url = `/#/sa-quote/${voucher.id}/edit/from-ref`;
                break;
            case 12:
            case 13:
            case 14:
                url = `/#/mb-teller-paper/${voucher.id}/edit/from-ref`;
                break;
        }
        window.open(url, '_blank');
    }

    getVoucherRefCatalogDTOByID(req) {
        const options = createRequestOption(req);
        return this.http
            .get<IAccountingObject[]>(SERVER_API_URL + 'api/catalog/get-voucher-ref', { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    checkQuantityExistsTest1(data: any[], account: any, postedDate: any) {
        const details = data.map(object => ({ ...object }));
        return new Promise(resolve => {
            const materialGoodsIDs: any[] = [];
            const repositoryIDs: any[] = [];
            const checkListMaterialGoodsID = new Map();
            const checkListRepositoryID = new Map();
            for (let i = 0; i < details.length; i++) {
                if (!details[i].repositoryID) {
                    if (details[i].fromRepositoryID) {
                        details[i].repositoryID = details[i].fromRepositoryID;
                    }
                    if (details[i].repository) {
                        details[i].repositoryID = details[i].repository.id;
                    }
                }
                if (!details[i].materialGoodsID) {
                    if (details[i].materialGood) {
                        details[i].materialGoodsID = details[i].materialGood.id;
                    }
                }
                if (details[i].materialGoodsID && !checkListMaterialGoodsID.get(details[i].materialGoodsID)) {
                    materialGoodsIDs.push(details[i].materialGoodsID);
                    checkListMaterialGoodsID.set(details[i].materialGoodsID, details[i].materialGoodsID);
                }
                // if (details[i].materialGood && !checkListMaterialGoodsID.get(details[i].materialGood.id)) {
                //     materialGoodsIDs.push(details[i].materialGood.id);
                //     checkListMaterialGoodsID.set(details[i].materialGood.id, details[i].materialGood.id);
                // }
                if (details[i].repositoryID && !checkListRepositoryID.get(details[i].repositoryID)) {
                    repositoryIDs.push(details[i].repositoryID);
                    checkListRepositoryID.set(details[i].repositoryID, details[i].repositoryID);
                }
                // if (details[i].repository && !checkListRepositoryID.get(details[i].repository.id)) {
                //     repositoryIDs.push(details[i].repository.id);
                //     checkListRepositoryID.set(details[i].repositoryID, details[i].repository.id);
                // }
            }

            let textCode: any = '';
            let textCode2: any = '';
            let materialGoodList: any[];
            let repositoryListError: any;
            let materialGoodListError: any;
            // nếu materialGoodsIDs hoặc repositoryIDs == null thì không giọi hàm
            if (materialGoodsIDs && materialGoodsIDs.length > 0 && repositoryIDs && repositoryIDs.length > 0) {
                const paramQuantityExistsTest = {
                    materialGoodsIDs: materialGoodsIDs ? materialGoodsIDs : '',
                    repositoryIDs: repositoryIDs ? repositoryIDs : '',
                    postedDate: postedDate ? (postedDate instanceof moment ? postedDate.format(DATE_FORMAT) : postedDate) : ''
                };
                this.viewVoucherService.getQuantityExistsTest(paramQuantityExistsTest).subscribe(res => {
                    const paramMaterialGoodAndRepository = {
                        materialGoodsIDs: materialGoodsIDs ? materialGoodsIDs : '',
                        repositoryIDs: repositoryIDs ? repositoryIDs : ''
                    };
                    this.viewVoucherService.getMaterialGoodAndRepository(paramMaterialGoodAndRepository).subscribe(req => {
                        // materialGoodListError = req.body.materialGoodIDS;
                        // repositoryListError = req.body.repositoryIDS;
                        materialGoodListError = new Map(req.body.materialGoodIDS.map(i => [i.id, i.name]));
                        repositoryListError = new Map(req.body.repositoryIDS.map(i => [i.id, i.name]));
                        materialGoodList = res.body;
                        if (materialGoodList && materialGoodList.length > 0) {
                            const mgForPPOderMap = new Map();
                            const mgForPPOderMap2 = new Map();
                            const mgForPPOderMinimumStockMap = new Map();
                            const mgForPPOderInStockMap = new Map();
                            for (let i = 0; i < details.length; i++) {
                                let item = null;
                                if (details[i].materialGoodsID) {
                                    item = materialGoodList.find(x => x.id === details[i].materialGoodsID);
                                } else {
                                    item = materialGoodList.find(x => x.id === details[i].materialGood.id);
                                }
                                if (!item) {
                                    if (details[i].materialGoodsType !== 2 && details[i].materialGoodsType !== 4)
                                        mgForPPOderInStockMap.set(
                                            materialGoodListError.get(details[i].materialGoodsID),
                                            this.translate.instant('ebwebApp.common.quantityExtensive', {
                                                materialGoodCode: materialGoodListError.get(details[i].materialGoodsID),
                                                repositoryCode: repositoryListError.get(details[i].repositoryID)
                                            })
                                        );
                                } else {
                                    if (item && item.materialGoodsType !== 2 && item.materialGoodsType !== 4) {
                                        // if (item.materialGoodsInStock < 0) {
                                        //     if (mgForPPOderMap.size === 0 || mgForPPOderMap.get(item.materialGoodsCode) !== item.materialGoodsCode) {
                                        //         mgForPPOderMap.set(item.materialGoodsCode, item.materialGoodsCode);
                                        //         textCode += item.materialGoodsCode + ', ';
                                        //     }
                                        // }
                                        // if (mgForPPOderMap.size === 0) {
                                        //     mgForPPOderMap.set(item.materialGoodsCode, details[i].mainQuantity);
                                        // }
                                        if (!mgForPPOderMap.has(item.materialGoodsCode)) {
                                            mgForPPOderMap.set(item.materialGoodsCode, details[i].mainQuantity);
                                        } else {
                                            mgForPPOderMap.set(
                                                item.materialGoodsCode,
                                                mgForPPOderMap.get(item.materialGoodsCode) + details[i].mainQuantity
                                            );
                                        }
                                        const itemInStock = this.round(item.materialGoodsInStock, account.systemOption, 3);
                                        const itemInStockSum = this.round(
                                            Number(mgForPPOderMap.get(item.materialGoodsCode)),
                                            account.systemOption,
                                            3
                                        );
                                        if (itemInStock - itemInStockSum < 0) {
                                            mgForPPOderInStockMap.set(
                                                item.materialGoodsCode,
                                                this.translate.instant('ebwebApp.common.quantityExtensive', {
                                                    materialGoodCode: materialGoodListError.get(details[i].materialGoodsID),
                                                    repositoryCode: repositoryListError.get(details[i].repositoryID)
                                                })
                                            );
                                        }
                                        if (item.minimumStock && item.minimumStock !== 0) {
                                            // if (mgForPPOderMap2.size === 0) {
                                            //     mgForPPOderMap2.set(item.materialGoodsCode, details[i].mainQuantity);
                                            // }
                                            if (!mgForPPOderMap2.has(item.materialGoodsCode)) {
                                                mgForPPOderMap2.set(item.materialGoodsCode, details[i].mainQuantity);
                                            } else {
                                                mgForPPOderMap2.set(
                                                    item.materialGoodsCode,
                                                    mgForPPOderMap2.get(item.materialGoodsCode) + details[i].mainQuantity
                                                );
                                            }
                                            if (
                                                item.materialGoodsInStock - Number(mgForPPOderMap2.get(item.materialGoodsCode)) <
                                                item.minimumStock
                                            ) {
                                                mgForPPOderMinimumStockMap.set(
                                                    item.materialGoodsCode,
                                                    this.translate.instant('ebwebApp.common.quantityExtensive', {
                                                        materialGoodCode: materialGoodListError.get(details[i].materialGoodsID),
                                                        repositoryCode: repositoryListError.get(details[i].repositoryID)
                                                    })
                                                );
                                            }
                                        }
                                    }
                                }
                            }
                            // số lượng tồn tối thiểu
                            for (const value of Array.from(mgForPPOderMinimumStockMap.values())) {
                                textCode2 += value + ', ';
                            }
                            // số lượng tồn
                            for (const value of Array.from(mgForPPOderInStockMap.values())) {
                                textCode += value + ', ';
                            }
                            resolve({
                                quantityExists: textCode ? textCode.slice(0, textCode.length - 2) : null,
                                minimumStock: textCode2 ? textCode2.slice(0, textCode2.length - 2) : null
                            });
                        } else {
                            // nếu tất cả mã hàng gửi lên đều không hợp lệ
                            const mgForPPOderInStockMap = new Map();
                            for (let i = 0; i < details.length; i++) {
                                if (!mgForPPOderInStockMap.get(details[i].materialGoodsCode)) {
                                    mgForPPOderInStockMap.set(
                                        details[i].materialGoodsCode,
                                        this.translate.instant('ebwebApp.common.quantityExtensive', {
                                            materialGoodCode: materialGoodListError.get(details[i].materialGoodsID),
                                            repositoryCode: repositoryListError.get(details[i].repositoryID)
                                        })
                                    );
                                }
                            }
                            for (const value of Array.from(mgForPPOderInStockMap.values())) {
                                textCode += value + ', ';
                            }
                            resolve({
                                quantityExists: textCode ? textCode.slice(0, textCode.length - 2) : null,
                                minimumStock: textCode2 ? textCode2.slice(0, textCode2.length - 2) : null
                            });
                        }
                    });
                });
            } else {
                resolve({
                    quantityExists: textCode ? textCode.slice(0, textCode.length - 2) : null,
                    minimumStock: textCode2 ? textCode2.slice(0, textCode2.length - 2) : null
                });
            }
        });
    }

    getDataFromDkkD(req): Observable<any> {
        const options = createRequestOption(req);
        return this.http
            .get<any>(SERVER_API_URL + 'api/get-dkkd', { params: options, observe: 'response' })
            .pipe(map((res: any) => this.convertDateFromServer(res)));
    }

    isPackageDemo(account) {
        if (account) {
            return account.ebPackage.packageCode.includes('DEMO');
        }
    }
}
