import { HostListener, Injectable, OnDestroy } from '@angular/core';
import { CategoryName, DBDateClosed } from 'app/app.constants';
import * as moment from 'moment';
import { DATE_FORMAT, DATE_FORMAT_SLASH } from 'app/shared';
import { IBank } from 'app/shared/model/bank.model';
import { IExpenseItem } from 'app/shared/model/expense-item.model';
import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';
import { ICostSet } from 'app/shared/model/cost-set.model';
import { IBudgetItem } from 'app/shared/model/budget-item.model';
import { IStatisticsCode } from 'app/shared/model/statistics-code.model';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { IMaterialGoods, IMaterialGoodsInStock } from 'app/shared/model/material-goods.model';
import { IMaterialGoodsCategory } from 'app/shared/model/material-goods-category.model';
import { ISAQuoteDetails } from 'app/shared/model/sa-quote-details.model';
import { IRepository } from 'app/shared/model/repository.model';
import { IUnit } from 'app/shared/model/unit.model';
import { ICreditCard } from 'app/shared/model/credit-card.model';
import { IAccountingObjectBankAccount } from 'app/shared/model/accounting-object-bank-account.model';
import { PPOrderDetail } from 'app/shared/model/pporderdetail.model';
import index from '@angular/cli/lib/cli';
import { ONBROADCASTEVENT } from 'app/muahang/mua-dich-vu/mua-dich-vu-event-name.constant';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { Subscription } from 'rxjs';
import { ITransportMethod } from 'app/shared/model/transport-method.model';
import { IBankAccountDetails } from 'app/shared/model/bank-account-details.model';

// @Component({
//     template: ``,
//     selector: 'eb-base'
// })
@Injectable()
export abstract class BaseComponent implements OnDestroy {
    isShowPopup: boolean;
    isEnter: any;
    utilsService: UtilsService;
    selectedRows = [];
    selectedRow = {};
    objects: any[];
    color: any;
    isEdit: boolean;
    isEditPage: boolean;
    toolbarClass: string;
    bankAccountDetails: any;
    bankPopup: IBank[];
    organizationUnits: IOrganizationUnit[];
    allOrganizationUnits: IOrganizationUnit[];
    costSets: ICostSet[];
    costSetList: ICostSet[];
    expenseItems: IExpenseItem[];
    expenseItemList: IExpenseItem[];
    statisticCodes: IStatisticsCode[];
    creditCards: ICreditCard[];
    creditCardType: string;
    ownerCard: string;
    budgetItems: IBudgetItem[];
    budgetItemList: IBudgetItem[];
    currentRow: number;
    name: string;
    details: any[];
    costSet: any;
    details2nd: any[];
    parent: any;
    isCbbSaveAndNew: boolean;
    accountingObjects: IAccountingObject[];
    accounting: IAccountingObject[];
    materialGoodsCategoryPopup: IMaterialGoodsCategory[];
    employees: IAccountingObject[];
    transportMethods: ITransportMethod[];
    materialGoodsInStock: IMaterialGoodsInStock[];
    sAQuoteDetails: ISAQuoteDetails[];
    repositoryPopup: IRepository[];
    repositorys: IRepository[];
    repositories: IRepository[];
    units: IUnit[];
    materialGoodss: IMaterialGoods[];
    accountingObjectBankAccounts: IAccountingObjectBankAccount[];
    nhanVienID: string;
    taiKhoanNganHangID: string;
    taiKhoanNganHang: any;
    nganHangID: string;
    theTinDung: string;
    eventSubscribers: Subscription[] = [];
    inPopup: any;

    canDeactive() {}

    getStyle(record?: boolean) {
        return !record
            ? {
                  input: { color: this.color },
                  color: this.color
              }
            : {};
    }

    @HostListener('document:keydown.control.s')
    save(isNew = false) {}

    @HostListener('document:keydown.control.o')
    saveAndNew() {}

    @HostListener('document:keydown.control.e')
    edit() {}

    @HostListener('document:keydown.control.d')
    delete() {}

    // @HostListener('document:keydown.control.d')
    // deleteUser() {}

    @HostListener('document:keydown.control.r')
    record() {}

    @HostListener('document:keydown.control.u')
    unrecord() {}

    @HostListener('document:keydown.control.p', ['$event'])
    print($event?) {}

    @HostListener('document:keydown.control.m')
    copyAndNew() {}

    @HostListener('document:keydown.control.b')
    closeForm() {}

    @HostListener('document:keydown.control.f', ['$event'])
    toggleSearch($event?) {}

    // @HostListener('document:keydown.control.insert', ['$event'])
    // addNewRow() {}
    //
    // @HostListener('document:keydown.control.delete', ['$event'])
    // deleteRow() {}
    //
    // @HostListener('document:keydown.control.c', ['$event'])
    // copyRow() {}

    @HostListener('document:keydown.control.x')
    export() {}

    @HostListener('document:keydown.control.i', ['$event'])
    addNew($event?) {}

    @HostListener('document:keydown.tab')
    tab() {
        if (
            this.objects &&
            this.selectedRow &&
            !window.location.href.includes('/edit') &&
            !window.location.href.includes('/new') &&
            (this.inPopup === undefined || this.inPopup === null || this.inPopup === false)
        ) {
            if (this.objects.length > 0 && this.selectedRow) {
                event.preventDefault();
                const selectedRowIndex = this.objects.indexOf(this.selectedRow);
                if (selectedRowIndex !== -1 && selectedRowIndex < this.objects.length - 1) {
                    this.selectedRow = this.objects[selectedRowIndex + 1];
                    this.selectedRows = [];
                    this.selectedRows.push(this.selectedRow);
                    this.onSelect(this.selectedRow);
                } else if (selectedRowIndex === this.objects.length - 1) {
                    this.selectedRow = this.objects[0];
                    this.selectedRows = [];
                    this.selectedRows.push(this.selectedRow);
                    this.onSelect(this.selectedRow);
                }
            }
        }
    }

    // Add by Hautv
    @HostListener('document:keydown.arrowUp')
    arrowUp() {
        if (this.objects && this.selectedRow && !window.location.href.includes('/edit')) {
            if (this.objects.length > 0 && this.selectedRow) {
                event.preventDefault();
                const selectedRowIndex = this.objects.indexOf(this.selectedRow);
                if (selectedRowIndex !== -1 && selectedRowIndex > 0) {
                    this.selectedRow = this.objects[selectedRowIndex - 1];
                    this.selectedRows = [];
                    this.selectedRows.push(this.selectedRow);
                    this.onSelect(this.selectedRow);
                } else if (selectedRowIndex === 0) {
                    this.selectedRow = this.objects[this.objects.length - 1];
                    this.selectedRows = [];
                    this.selectedRows.push(this.selectedRow);
                    this.onSelect(this.selectedRow);
                }
            }
        }
    }

    // Add by Hautv
    @HostListener('document:keydown.arrowDown')
    arrowDown() {
        if (this.objects && this.selectedRow && !window.location.href.includes('/edit')) {
            if (this.objects.length > 0 && this.selectedRow) {
                event.preventDefault();
                const selectedRowIndex = this.objects.indexOf(this.selectedRow);
                if (selectedRowIndex !== -1 && selectedRowIndex < this.objects.length - 1) {
                    this.selectedRow = this.objects[selectedRowIndex + 1];
                    this.selectedRows = [];
                    this.selectedRows.push(this.selectedRow);
                    this.onSelect(this.selectedRow);
                } else if (selectedRowIndex === this.objects.length - 1) {
                    this.selectedRow = this.objects[0];
                    this.selectedRows = [];
                    this.selectedRows.push(this.selectedRow);
                    this.onSelect(this.selectedRow);
                }
            }
        }
    }

    // Add by Hautv
    @HostListener('document:keydown.enter')
    enter() {
        event.preventDefault();
        if (this.objects && this.selectedRow && !window.location.href.includes('/edit')) {
            this.doubleClickRow(this.selectedRow);
        }
    }

    doubleClickRow(any?, any2?) {}

    // Add by Hautv
    @HostListener('document:keydown.control.a')
    selectAll() {
        if (this.objects && !window.location.href.includes('/edit')) {
            event.preventDefault();
            this.selectedRows = [];
            this.objects.forEach(n => {
                this.selectedRows.push(n);
            });
        }
    }

    selectMultiRow(object, evt, objects: any[]) {
        if (evt) {
            evt.preventDefault();
            if (evt.shiftKey) {
                this.selectedRows = [];
                if (this.selectedRow) {
                    const previousIndex = objects.indexOf(this.selectedRow);
                    const thisIndex = objects.indexOf(object);

                    // Nếu lần trước và lần này cùng chọn 1 vị trí, add hoặc remove object tùy theo object không thuộc
                    // hoặc thuộc list

                    if (previousIndex === thisIndex) {
                        const curIndex = this.selectedRows.indexOf(object);
                        if (curIndex !== -1) {
                            this.selectedRows.splice(curIndex, 1);
                        } else {
                            this.selectedRows.push(object);
                        }
                    } else {
                        // Nếu 2 vị trí chọn khác nhau,
                        // sắp xếp vị trí trước sau để chạy vòng for
                        // Nếu lần chọn 2 đã thuộc list selected thì loại bỏ tất cả khỏi list selected
                        // Nếu lần chọn 2 không thuộc list selected thì thêm tất cả vào list selected
                        let start = 0;
                        let end = 0;
                        if (thisIndex >= previousIndex) {
                            start = previousIndex;
                            end = thisIndex;
                        } else {
                            start = thisIndex;
                            end = previousIndex;
                        }
                        const curIndex = this.selectedRows.indexOf(object);

                        for (let i = start; i <= end; i++) {
                            if (curIndex === -1) {
                                this.selectedRows.push(objects[i]);
                            } else {
                                for (let j = 0; j < this.selectedRows.length; j++) {
                                    if (this.selectedRows[j].id === objects[i].id) {
                                        this.selectedRows.splice(j, 1);
                                        j--;
                                    }
                                }
                            }
                        }
                    }
                    // phuonghv: Chỉ select từ phần tử đầu tiên khi nhấm shift
                    // this.selectedRow = object;
                } else {
                    this.selectedRows = [];
                    this.selectedRows.push(object);
                    this.selectedRow = object;
                }
            } else if (evt.ctrlKey || evt.metaKey) {
                const _index = this.selectedRows.indexOf(object);
                if (_index !== -1) {
                    this.selectedRows.splice(_index, 1);
                } else {
                    this.selectedRows.push(object);
                }
                this.selectedRow = object;
            } else {
                this.selectedRows = [];
                this.selectedRows.push(object);
                this.selectedRow = object;
            }
        } else {
            this.selectedRows = [];
            this.selectedRows.push(object);
            this.selectedRow = object;
        }
        this.onSelect(object);
    }

    onSelect(object?, any?) {}

    moveArrowDown($event, count) {
        const inputs = document.getElementsByTagName('input');
        const selects = document.getElementsByTagName('select');
        const curIndex = $event.path['0'].tabIndex;
        let nextIndex = curIndex;
        let j = 0;
        let isFound = false;
        while (j < 15) {
            j++;
            nextIndex = curIndex + count;
            for (let i = 0; i < inputs.length; i++) {
                // loop over the tabs.
                if (inputs[i].tabIndex === nextIndex && !inputs[i].disabled) {
                    // is this our tab?
                    inputs[i].focus(); // Focus and leave.
                    isFound = true;
                    break;
                }
            }
            for (let i = 0; i < selects.length; i++) {
                // loop over the tabs.
                if (selects[i].tabIndex === nextIndex && !selects[i].disabled) {
                    // is this our tab?
                    selects[i].focus(); // Focus and leave.
                    isFound = true;
                    break;
                }
            }
            if (isFound) {
                break;
            }
        }
    }

    moveArrowUp($event, count) {
        const inputs = document.getElementsByTagName('input');
        const selects = document.getElementsByTagName('select');
        const curIndex = $event.path['0'].tabIndex;
        let nextIndex = curIndex;
        let j = 0;
        let isFound = false;
        while (j < 15) {
            j++;
            nextIndex = curIndex - count;
            for (let i = 0; i < inputs.length; i++) {
                // loop over the tabs.
                if (inputs[i].tabIndex === nextIndex && !inputs[i].disabled) {
                    // is this our tab?
                    inputs[i].focus(); // Focus and leave.
                    isFound = true;
                    break;
                }
            }
            for (let i = 0; i < selects.length; i++) {
                // loop over the tabs.
                if (selects[i].tabIndex === nextIndex && !selects[i].disabled) {
                    // is this our tab?
                    selects[i].focus(); // Focus and leave.
                    isFound = true;
                    break;
                }
            }
            if (isFound) {
                break;
            }
        }
    }

    focusFirstInput() {
        const inputs = document.getElementsByTagName('input');
        if (
            window.location.href.includes('supplier') ||
            window.location.href.includes('accounting-object') ||
            window.location.href.includes('repository')
        ) {
            inputs[1].focus();
        } else if (
            window.location.href.includes('mua-hang/qua-kho') ||
            window.location.href.includes('mua-hang/khong-qua-kho') ||
            window.location.href.includes('mua-dich-vu')
        ) {
            inputs[2].focus();
        } else {
            inputs[0].focus();
        }
    }

    disableInput() {
        if (window.location.href.includes('new') || window.location.href.includes('edit') || this.isEditPage) {
            const inputs = document.getElementsByTagName('input');
            const buttons = document.getElementsByTagName('button');
            const selects = document.getElementsByTagName('select');
            const toolbar = document.getElementsByClassName(this.toolbarClass ? this.toolbarClass : 'gr-tool-bar2')[0];

            const toolbarButton = toolbar !== null ? toolbar.getElementsByTagName('button') : [];

            const tfoot = document.getElementsByTagName('tfoot');
            for (let i = 0; i < inputs.length; i++) {
                if (!inputs[i].className.includes('noSetDisable') && !inputs[i].className.includes('modal-body')) {
                    inputs[i].disabled = !this.isEdit;
                    if (!inputs[i].className.includes('noEdit')) {
                        if (inputs[i].disabled) {
                            if (inputs[i].className.includes('input-Cell')) {
                                inputs[i].classList.remove('bg-white');
                                inputs[i].classList.add('bg-inherit');
                            } else {
                                inputs[i].classList.add('bg-white');
                            }
                        }
                    } else {
                        inputs[i].disabled = true;
                        if (
                            !inputs[i].parentElement.className.includes('countRow') &&
                            !inputs[i].parentElement.className.includes('color-tfoot')
                        ) {
                            inputs[i].classList.add('bg-white');
                        }
                    }
                }
            }
            for (let i = 0; i < buttons.length; i++) {
                if (
                    !buttons[i].parentElement.className.includes('modal-footer') &&
                    !buttons[i].parentElement.className.includes('modal-body') &&
                    !buttons[i].className.includes('noSetDisable')
                ) {
                    let check = true;
                    for (let j = 0; j < toolbarButton.length; j++) {
                        if (buttons[i] === toolbarButton[j]) {
                            check = false;
                        }
                    }
                    if (check) {
                        buttons[i].disabled = !this.isEdit;
                    }
                    if (buttons[i].className.includes('noEdit')) {
                        buttons[i].disabled = true;
                    }
                    if (buttons[i].className.includes('navbar-toggler')) {
                        buttons[i].disabled = false;
                    }
                }
            }
            for (let i = 0; i < selects.length; i++) {
                if (!selects[i].className.includes('noSetDisable')) {
                    selects[i].disabled = !this.isEdit;
                    if (selects[i].className.includes('noEdit')) {
                        selects[i].disabled = true;
                    }
                    if (selects[i].disabled) {
                        selects[i].classList.add('bg-white');
                    }
                }
            }
            if (tfoot) {
                for (let i = 0; i < tfoot.length; i++) {
                    const tfootInput = tfoot[i].getElementsByTagName('input');
                    for (let j = 0; j < tfootInput.length; j++) {
                        tfootInput[j].disabled = true;
                    }
                }
            }
        }
    }

    getSelectionText() {
        let selectedText = '';
        if (window.getSelection) {
            selectedText = window.getSelection().toString();
        } else if (document.getSelection) {
            selectedText = document.getSelection().toString();
        }
        return selectedText;
    }

    checkCloseBook(account, postedDate: any): Boolean {
        if (postedDate && !(postedDate instanceof moment) && postedDate.toString().includes('/')) {
            postedDate = moment(postedDate, DATE_FORMAT_SLASH);
        }
        if (account && postedDate) {
            if (!(postedDate instanceof moment)) {
                postedDate = moment(postedDate);
            }
            const dbDateClosed = account.systemOption.find(x => x.code === DBDateClosed).data;
            if (dbDateClosed && postedDate) {
                const dateClose = dbDateClosed ? moment(dbDateClosed, DATE_FORMAT) : null;
                if (dateClose) {
                    if (postedDate.isAfter(dateClose)) {
                        return false;
                    } else {
                        return true;
                    }
                } else {
                    return false;
                }
            }
        }
    }

    @HostListener('document:keydown.ArrowUp', ['$event'])
    @HostListener('document:keydown.ArrowDown', ['$event'])
    /***
     *
     * @param $event
     * @param scroll 'UP' or 'DOWN' only
     */
    scrollItemSelected($event: KeyboardEvent, scroll?: string) {
        // class table
        const parentContainer = document.getElementsByClassName('voucher-table')[0];
        // class item selected
        const element = document.getElementsByClassName('selected')[0];
        // class header
        const headerTable = document.getElementsByClassName('header-table')[0];
        if (parentContainer === undefined || element === undefined || headerTable === undefined) {
            return false;
        }
        const elRect = element.getBoundingClientRect(),
            parRect = parentContainer.getBoundingClientRect(),
            headerRect = headerTable.getBoundingClientRect();
        const elementHeight = elRect.height;
        if (scroll && scroll === 'UP') {
            if (elRect.top <= parRect.top + elementHeight) {
                parentContainer.scrollTop = parentContainer.scrollTop - (parRect.top + elementHeight - elRect.top) - headerRect.height;
            }
        }
        if (scroll && scroll === 'DOWN') {
            if (!(elRect.top >= parRect.top && elRect.bottom <= parRect.bottom && elRect.bottom + elementHeight <= parRect.bottom)) {
                parentContainer.scrollTop = parentContainer.scrollTop + elRect.height + (elRect.bottom - parRect.bottom);
            }
        }
        if ($event && $event.key === 'ArrowDown') {
            // down arrow
            if (!(elRect.top >= parRect.top && elRect.bottom <= parRect.bottom && elRect.bottom + elementHeight <= parRect.bottom)) {
                parentContainer.scrollTop = parentContainer.scrollTop + elRect.height + (elRect.bottom - parRect.bottom);
            }
        } else {
            // up arrow
            if (elRect.top <= parRect.top + elementHeight) {
                parentContainer.scrollTop = parentContainer.scrollTop - (parRect.top + elementHeight - elRect.top) - headerRect.height;
            }
        }
    }

    registerComboboxSave(response) {
        console.trace();
        if (response.content.name === CategoryName.TAI_KHOAN_NGAN_HANG) {
            const newBankAccountDetail = response.content.data;
            if (newBankAccountDetail.isActive) {
                this.bankAccountDetails.push(newBankAccountDetail);
                this.bankAccountDetails = this.bankAccountDetails.sort((a, b) => a.bankAccount.localeCompare(b.bankAccount));
                if (!this.isCbbSaveAndNew) {
                    if (this.currentRow !== null && this.currentRow !== undefined) {
                        this.details[this.currentRow].bankAccountDetailID = newBankAccountDetail.id;
                        this.details[this.currentRow].bankAccountDetails = newBankAccountDetail;
                    } else if (this.parent) {
                        this.parent.bankAccountDetailID = newBankAccountDetail.id;
                        this.parent.accountPaymentId = newBankAccountDetail.id;
                        this.parent.bankName = newBankAccountDetail.bankName;
                        this.parent.accountPayment = newBankAccountDetail;
                        this.onChangeAccountPayment();
                    } else {
                        this.taiKhoanNganHangID = newBankAccountDetail.id;
                        this.taiKhoanNganHang = newBankAccountDetail;
                    }
                }
            }
        }
        if (response.content.name === CategoryName.NGAN_HANG) {
            const newBank = response.content.data;
            if (newBank.bank.isActive) {
                this.bankPopup.push(newBank.bank);
                this.bankPopup = this.bankPopup.sort((a, b) => a.bankCode.localeCompare(b.bankCode));
                if (!this.isCbbSaveAndNew) {
                    if (this.parent) {
                        this.parent.id = newBank.bank.id;
                    } else {
                        this.nganHangID = newBank.id;
                    }
                }
            }
        }
        if (response.content.name === CategoryName.THE_TIN_DUNG) {
            const newCreditCard = response.content.data;
            if (newCreditCard.isActive) {
                if (this.creditCards) {
                    this.creditCards.push(newCreditCard);
                    this.creditCards = this.creditCards.sort((a, b) => a.creditCardNumber.localeCompare(b.creditCardNumber));
                } else {
                    this.bankAccountDetails.push(newCreditCard);
                    this.bankAccountDetails = this.bankAccountDetails.sort((a, b) => a.bankName.localeCompare(b.bankName));
                }
                if (!this.isCbbSaveAndNew) {
                    if (this.parent) {
                        this.parent.creditCardNumber = newCreditCard.creditCardNumber;
                        this.parent.creditCardID = newCreditCard.id;
                        this.parent.creditCardItem = newCreditCard;
                        this.ownerCard = newCreditCard.ownerCard;
                        this.creditCardType = newCreditCard.creditCardType;
                        this.onChangeCreditCard();
                    } else {
                        this.theTinDung = newCreditCard;
                    }
                }
            }
        }
        if (response.content.name === CategoryName.PHONG_BAN) {
            const newOrganizationUnit = response.content.data;
            if (newOrganizationUnit.isActive) {
                this.organizationUnits.push(newOrganizationUnit);
                this.organizationUnits = this.organizationUnits.sort((a, b) =>
                    a.organizationUnitCode.localeCompare(b.organizationUnitCode)
                );
                if (!this.isCbbSaveAndNew) {
                    if (this.currentRow !== null || this.currentRow !== undefined) {
                        this.details[this.currentRow].departmentID = newOrganizationUnit.id;
                        this.details[this.currentRow].departmentId = newOrganizationUnit.id;
                        this.details[this.currentRow].department = newOrganizationUnit;
                        this.details[this.currentRow].organizationUnit = newOrganizationUnit;
                        this.details[this.currentRow].organizationUnitItem = newOrganizationUnit;
                        this.selectChangeOrganizationUnitItem(this.details[this.currentRow]);
                    } else {
                    }
                }
            }
        }
        if (response.content.name === CategoryName.MA_THONG_KE) {
            const newStatisticsCode = response.content.data;
            if (newStatisticsCode.isActive) {
                this.statisticCodes.push(newStatisticsCode);
                this.statisticCodes = this.statisticCodes.sort((a, b) => a.statisticsCode.localeCompare(b.statisticsCode));
                if (!this.isCbbSaveAndNew) {
                    if (this.currentRow !== null && this.currentRow !== undefined) {
                        this.details[this.currentRow].statisticsCodeID = newStatisticsCode.id;
                        this.details[this.currentRow].statisticCodeID = newStatisticsCode.id;
                        this.details[this.currentRow].statisticsId = newStatisticsCode.id;
                        this.details[this.currentRow].statisticCode = newStatisticsCode;
                        this.details[this.currentRow].statisticsCode = newStatisticsCode;
                        this.details[this.currentRow].statisticsCodeItem = newStatisticsCode;
                        this.selectChangeStatisticsCodeItem(this.details[this.currentRow]);
                    } else {
                    }
                }
            }
        }
        if (response.content.name === CategoryName.DOI_TUONG_TAP_HOP_CHI_PHI) {
            const newCostSet = response.content.data;
            if (newCostSet.isActive) {
                if (this.costSetList) {
                    this.costSetList.push(newCostSet);
                    this.costSetList = this.costSetList.sort((a, b) => a.costSetCode.localeCompare(b.costSetCode));
                } else {
                    this.costSets.push(newCostSet);
                    this.costSets = this.costSets.sort((a, b) => a.costSetCode.localeCompare(b.costSetCode));
                }
                if (!this.isCbbSaveAndNew) {
                    if (this.currentRow !== null && this.currentRow !== undefined) {
                        this.details[this.currentRow].costSetID = newCostSet.id;
                        this.details[this.currentRow].costSetId = newCostSet.id;
                        this.details[this.currentRow].costSet = newCostSet;
                        this.details[this.currentRow].costSetItem = newCostSet;
                        this.selectChangeCostSetItem(this.details[this.currentRow]);
                    } else {
                    }
                }
            }
        }
        if (response.content.name === CategoryName.MUC_THU_CHI) {
            const newBudgetItem = response.content.data;
            if (newBudgetItem.isActive && newBudgetItem.id) {
                if (this.budgetItemList) {
                    this.budgetItemList.push(newBudgetItem);
                    this.budgetItemList = this.budgetItemList.sort((a, b) => a.budgetItemCode.localeCompare(b.budgetItemCode));
                } else {
                    this.budgetItems.push(newBudgetItem);
                    this.budgetItems = this.budgetItems.sort((a, b) => a.budgetItemCode.localeCompare(b.budgetItemCode));
                }
                if (!this.isCbbSaveAndNew) {
                    if (this.currentRow !== null && this.currentRow !== undefined) {
                        this.details[this.currentRow].budgetItemID = newBudgetItem.id;
                        this.details[this.currentRow].budgetItemId = newBudgetItem.id;
                        this.details[this.currentRow].budgetItem = newBudgetItem;
                        this.selectChangeBudgetItem(this.details[this.currentRow]);
                    } else {
                    }
                }
            }
        }
        if (response.content.name === CategoryName.KHOAN_MUC_CHI_PHI) {
            const newExpenseItem = response.content.data;
            if (newExpenseItem.isActive && newExpenseItem.id) {
                if (this.expenseItemList) {
                    this.expenseItemList.push(newExpenseItem);
                    this.expenseItemList = this.expenseItemList.sort((a, b) => a.expenseItemCode.localeCompare(b.expenseItemCode));
                } else {
                    this.expenseItems.push(newExpenseItem);
                    this.expenseItems = this.expenseItems.sort((a, b) => a.expenseItemCode.localeCompare(b.expenseItemCode));
                }
                if (!this.isCbbSaveAndNew) {
                    if (this.currentRow !== null && this.currentRow !== undefined) {
                        if (this.details) {
                            this.details[this.currentRow].expenseItemID = newExpenseItem.id;
                            this.details[this.currentRow].expenseItemId = newExpenseItem.id;
                            this.details[this.currentRow].expenseItem = newExpenseItem;
                            this.selectChangeExpenseItem(this.details[this.currentRow]);
                        } else if (this.details2nd) {
                            this.details2nd[this.currentRow].expenseItemID = newExpenseItem.id;
                            this.details2nd[this.currentRow].expenseItemId = newExpenseItem.id;
                            this.details2nd[this.currentRow].expenseItem = newExpenseItem;
                        }
                    } else {
                    }
                }
            }
        }
        if (
            response.content.name === CategoryName.KHACH_HANG ||
            response.content.name === CategoryName.NHA_CUNG_CAP ||
            response.content.name === CategoryName.DOI_TUONG ||
            (response.content.name === CategoryName.NHAN_VIEN && response.content.typeObject)
        ) {
            const newAccountingObject = response.content.data;
            if (newAccountingObject.isActive && newAccountingObject.id) {
                if (this.accounting && this.accounting.length > 0) {
                    this.accounting.push(newAccountingObject);
                    this.accounting = this.accounting.sort((a, b) => a.accountingObjectCode.localeCompare(b.accountingObjectCode));
                }
                if (this.parent && this.parent.accountingObjectType !== undefined && this.parent.accountingObjectType !== null) {
                    if (
                        newAccountingObject.objectType === this.parent.accountingObjectType ||
                        newAccountingObject.objectType == 2 ||
                        newAccountingObject.isEmployee
                    ) {
                        this.accountingObjects.push(newAccountingObject);
                        this.accountingObjects = this.accountingObjects.sort((a, b) =>
                            a.accountingObjectCode.localeCompare(b.accountingObjectCode)
                        );
                        this.accountingObjectBankAccounts = newAccountingObject.accountingObjectBankAccounts;
                        if (!this.isCbbSaveAndNew) {
                            if (this.currentRow !== null && this.currentRow !== undefined) {
                                this.details[this.currentRow].accountingObjectID = newAccountingObject.id;
                                this.details[this.currentRow].accountingObject = newAccountingObject;
                            } else {
                                // if (this.parent.accountingObjectID) {
                                this.parent.accountingObjectID = newAccountingObject.id;
                                // }
                                this.parent.accountingObject = newAccountingObject;
                                this.parent.accountingObjectID = newAccountingObject.id;
                                this.parent.accountingObjectName = newAccountingObject.accountingObjectName;
                                this.parent.accountingObjectAddress = newAccountingObject.accountingObjectAddress;
                            }
                        }
                    }
                } else {
                    this.accountingObjects.push(newAccountingObject);
                    if (this.accounting && this.accounting.length > 0) {
                        this.accounting.push(newAccountingObject);
                        this.accounting = this.accounting.sort((a, b) => a.accountingObjectCode.localeCompare(b.accountingObjectCode));
                    }
                    this.accountingObjectBankAccounts = newAccountingObject.accountingObjectBankAccounts;
                    if (!this.isCbbSaveAndNew) {
                        if (this.currentRow !== null && this.currentRow !== undefined) {
                            this.details[this.currentRow].accountingObjectID = newAccountingObject.id;
                            this.details[this.currentRow].accountingObject = newAccountingObject;
                            if (this.constructor.name === 'TabInsertUpdateReceiptMuaDichVuComponent') {
                                this.details[this.currentRow].postedObjectId = newAccountingObject.id;
                            }
                        } else {
                            // if (this.parent.accountingObjectID) {
                            this.parent.accountingObjectID = newAccountingObject.id;
                            // }
                            this.parent.accountingObject = newAccountingObject;
                            this.parent.accountingObjectID = newAccountingObject.id;
                            this.parent.accountingObjectName = newAccountingObject.accountingObjectName;
                            this.parent.accountingObjectAddress = newAccountingObject.accountingObjectAddress;
                            this.parent.accountingObject.accountingObjectName = newAccountingObject.accountingObjectName;
                            this.parent.accountingObject.accountingObjectAddress = newAccountingObject.accountingObjectAddress;
                            this.selectAccountingObjects();
                            this.selectAccountingObject();
                            this.onSelectAccountingObject();
                        }
                    } else {
                        this.parent.accountingObject = newAccountingObject;
                        this.details = undefined;
                    }
                }
            }
        }
        if (response.content.name === CategoryName.LOAI_VAT_TU_HANG_HOA) {
            const newMaterialGoodsCategories = response.content.data;
            if (newMaterialGoodsCategories.isActive && newMaterialGoodsCategories.id) {
                this.materialGoodsCategoryPopup.push(newMaterialGoodsCategories);
                this.materialGoodsCategoryPopup = this.materialGoodsCategoryPopup.sort((a, b) =>
                    a.materialGoodsCategoryCode.localeCompare(b.materialGoodsCategoryCode)
                );
                if (!this.isCbbSaveAndNew) {
                    if (this.currentRow !== null && this.currentRow !== undefined) {
                        this.details[this.currentRow].materialGoodsCategoryID = newMaterialGoodsCategories.id;
                    } else {
                        this.parent.materialGoodsCategoryID = newMaterialGoodsCategories.id;
                    }
                }
            }
        }
        if (response.content.name === CategoryName.VAT_TU_HANG_HOA) {
            const newMaterialGoods = response.content.data;
            if (newMaterialGoods.isActive && newMaterialGoods.id) {
                if (this.materialGoodsInStock) {
                    this.materialGoodsInStock.push(newMaterialGoods);
                } else {
                    if (this.materialGoodss) {
                        this.materialGoodss.push(newMaterialGoods);
                    }
                }
                if (!this.isCbbSaveAndNew) {
                    if (this.currentRow !== null && this.currentRow !== undefined) {
                        this.details[this.currentRow].materialGoods = newMaterialGoods;
                        this.details[this.currentRow].materialGoodsId = newMaterialGoods.id;
                        this.details[this.currentRow].materialGoodsID = newMaterialGoods.id;
                        this.details[this.currentRow].mgForPPOderItem = newMaterialGoods;
                        this.details[this.currentRow].description = newMaterialGoods.materialGoodsName;
                        this.details[this.currentRow].materialGoodsName = newMaterialGoods.materialGoodsName;
                        this.details[this.currentRow].materialGoodsCode = newMaterialGoods.materialGoodsCode;
                        this.details[this.currentRow].materialGood = newMaterialGoods;
                        this.selectChangeMaterialGoods(this.details[this.currentRow]);
                        this.selectedMaterialGoods(this.currentRow);
                        this.getListUnits(true);
                    } else if (this.parent && this.costSet) {
                        this.parent.materialGoodsID = newMaterialGoods.id;
                        this.parent.materialGoodsName = newMaterialGoods.materialGoodsName;
                        this.parent.materialGoodsCode = newMaterialGoods.materialGoodsCode;
                        this.costSet.materialGoods = newMaterialGoods;
                    }
                } else {
                    this.details[this.currentRow].materialGoods = newMaterialGoods;
                }
            }
            this.selectOnChangeMaterialGoods();
        }
        if (response.content.name === CategoryName.KHO) {
            const newRepository = response.content.data;
            if (newRepository.isActive && newRepository.id) {
                if (this.repositorys) {
                    this.repositorys.push(newRepository);
                    this.repositorys = this.repositorys.sort((a, b) => a.repositoryCode.localeCompare(b.repositoryCode));
                } else if (this.repositories) {
                    this.repositories.push(newRepository);
                    this.repositories = this.repositories.sort((a, b) => a.repositoryCode.localeCompare(b.repositoryCode));
                } else {
                    this.repositoryPopup.push(newRepository);
                    this.repositoryPopup = this.repositoryPopup.sort((a, b) => a.repositoryCode.localeCompare(b.repositoryCode));
                }
                if (!this.isCbbSaveAndNew) {
                    if (this.currentRow !== null && this.currentRow !== undefined) {
                        this.details[this.currentRow].repositoryID = newRepository.id;
                        this.details[this.currentRow].repository = newRepository;
                        this.onSelectRepository(this.details[this.currentRow]);
                    } else {
                        this.parent.repositoryID = newRepository.id;
                    }
                }
            }
        }
        if (response.content.name === CategoryName.DON_VI_TINH) {
            const newUnit = response.content.data;
            if (newUnit.isActive && newUnit.id) {
                if (this.units) {
                    this.units.push(newUnit);
                    this.units = this.units.sort((a, b) => a.unitName.localeCompare(b.unitName));
                }
                if (!this.isCbbSaveAndNew) {
                    if (this.currentRow !== null && this.currentRow !== undefined) {
                        this.details[this.currentRow].unitID = newUnit.id;
                        this.details[this.currentRow].unit = newUnit;
                    } else {
                        if (this.parent) {
                            this.parent.unitID = newUnit.id;
                        }
                    }
                }
            }
        }
        if (response.content.name === CategoryName.NHAN_VIEN && !response.content.typeObject) {
            const newEmployee = response.content.data;
            if (newEmployee.isActive && newEmployee.id) {
                this.employees.push(newEmployee);
                this.employees = this.employees.sort((a, b) => a.accountingObjectCode.localeCompare(b.accountingObjectCode));
                if (this.accounting && this.accounting.length > 0) {
                    this.accounting.push(newEmployee);
                    this.accounting = this.accounting.sort((a, b) => a.accountingObjectCode.localeCompare(b.accountingObjectCode));
                }
                if (!this.isCbbSaveAndNew) {
                    if (this.currentRow !== null && this.currentRow !== undefined) {
                        this.details[this.currentRow].employeeID = newEmployee.id;
                        this.details[this.currentRow].accountingObjectID = newEmployee.id;
                        this.details[this.currentRow].accountingObject = newEmployee;
                        this.selectEmployeeMC(this.details[this.currentRow]);
                    } else if (this.parent) {
                        this.parent.employeeID = newEmployee.id;
                        this.parent.accountingObjectIsEmployee = newEmployee;
                        this.parent.accountingObjectEmployee = newEmployee;
                        this.parent.employee = newEmployee;
                        this.selectEmployee();
                    } else {
                        this.nhanVienID = newEmployee.id;
                    }
                }
            }
        }
        if (response.content.name === CategoryName.PHUONG_THUC_VAN_CHUYEN) {
            const newTransportMethod = response.content.data;
            if (newTransportMethod.isActive && newTransportMethod.id) {
                if (this.transportMethods) {
                    this.transportMethods.push(newTransportMethod);
                }
                if (!this.isCbbSaveAndNew) {
                    if (this.currentRow !== null && this.currentRow !== undefined) {
                    } else {
                        if (this.parent) {
                            this.parent.transportMethodID = newTransportMethod;
                        }
                    }
                }
            }
        }
        this.addDataToDetail();
        this.addDataToDetail2nd();
    }

    addDataToDetail() {}

    addDataToDetail2nd() {}

    /*KHACH_HANG*/
    selectAccountingObjects() {}

    selectAccountingObject() {}

    onSelectAccountingObject() {}

    /* NHAN_VIEN*/
    selectEmployee() {}
    selectEmployeeMC(detail) {}

    /*VAT_TU_HANG_HOA*/
    selectChangeMaterialGoods(detail) {}

    selectOnChangeMaterialGoods() {}

    selectedMaterialGoods(_index) {}

    /*KHO*/
    onSelectRepository(detail) {}

    /*KHOAN_MUC_CHI_PHI*/
    selectChangeExpenseItem(detail) {}

    /*DOI_TUONG_TAP_HOP_CHI_PHI*/
    selectChangeCostSetItem(detail) {}

    /*MUC_THU_CHI*/
    selectChangeBudgetItem(detail) {}

    /*PHONG_BAN*/
    selectChangeOrganizationUnitItem(detail) {}

    /*MA_THONG_KE*/
    selectChangeStatisticsCodeItem(detail) {}

    /*TAI_KHOAN_NGAN_HANG*/
    onChangeAccountPayment() {}

    /*THE_TIN_DUNG*/
    onChangeCreditCard() {}

    getListUnits(quickAdd?) {}

    // add by Hautv
    ngOnDestroy(): void {
        if (this.eventSubscribers) {
            this.eventSubscribers.forEach(ev => {
                ev.unsubscribe();
            });
        }
    }
}
