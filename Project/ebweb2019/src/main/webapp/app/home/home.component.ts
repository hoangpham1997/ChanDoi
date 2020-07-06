import { Component, OnDestroy, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IUser, LoginModalService, Principal, UserService } from 'app/core';
import { Router } from '@angular/router';
import { InfoPackage } from 'app/shared/model/info-package.model';
import { HomeService } from 'app/home/home.service';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { DDSo_NCachHangDVi, DDSo_NCachHangNghin, DDSo_SoAm, DDSo_TienVND, WarningPackage } from 'app/app.constants';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { ITreeAccountList } from 'app/shared/model/account-list-tree.model';
import { IAccountList } from 'app/shared/model/account-list.model';
import { AccountListService } from 'app/danhmuc/account-list';
import { Subscription } from 'rxjs';
import * as moment from 'moment';
import { Moment } from 'moment';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { DATE_FORMAT, DATE_FORMAT_SEARCH, DATE_FORMAT_SLASH, DATE_FORMAT_TYPE1 } from 'app/shared';
import * as pluginDataLabels from 'chartjs-plugin-datalabels';
import { ChartDataSets, ChartPoint } from 'chart.js';
import { BaseChartDirective, Label } from 'ng2-charts';
import { ROLE } from 'app/role.constants';
// import * as pluginDataLabels from 'chartjs-plugin-labels';

// import 'chart.piecelabel.js';

@Component({
    selector: 'eb-home',
    templateUrl: './home.component.html',
    styleUrls: ['home.css']
})
export class HomeComponent implements OnInit, OnDestroy {
    @ViewChild(BaseChartDirective) chart: BaseChartDirective;
    @ViewChild('activeAlert') activeAlert: TemplateRef<any>;
    account: any;
    modalRef: NgbModalRef;
    infoPackage: InfoPackage;
    selectedRow: ITreeAccountList;
    treeAccountList: ITreeAccountList[];
    listParentAccount: ITreeAccountList[];
    flatTreeAccountList: ITreeAccountList[];
    listTHead: string[];
    listKey: any[];
    navigateForm: string;
    accountLists: IAccountList[];
    eventSubscriber: Subscription;
    sucKhoeTaiChinhs: any[];
    objTimeLine: { dtBeginDate?: string; dtEndDate?: string };
    nganCachHangNghin: any;
    nganCachHangDonVi: any;
    DDSo_SoAm: any;
    DDSo_NgoaiTe: any;
    DDSo_TienVND: any;
    /*Suc khoe tai chinh*/
    fromDateSKTC: Moment;
    toDateSKTC: Moment;
    listTimeLine: any;
    timeLineVoucherSKTC: any;
    /*Biểu đồ tổng hợp*/
    timeLineVoucherBDTH: any;
    fromDateBDTH: Moment;
    toDateBDTH: Moment;
    idTab: number;
    ebUserPackageStatus: number;
    ngbModalRef: any;
    showlabel = false;
    public barChartPlugins = [pluginDataLabels];
    chartTypeBDTH = 'bar';
    dataBDTH: any[];
    private _user: IUser;
    public chartDatasetsBDTH: ChartDataSets[] = [{ data: [], label: '' }];
    public chartLabelsBDTH: any[];
    public chartColorsBDTH: Array<any> = [
        {
            backgroundColor: ['#1AC13A', '#F9AA1B', '#DE2C3A', '#189FB3'],
            borderColor: ['#1AC13A', '#F9AA1B', '#DE2C3A', '#189FB3'],
            borderWidth: 2
        }
    ];

    ROLE_TMNH = ROLE.TienMatNganHang;
    ROLE_MH = ROLE.MuaHang;
    ROLE_BH = ROLE.BanHang;
    ROLE_KHO = ROLE.Kho;
    ROLE_TH = ROLE.TongHop;
    ROLE_QLHD = ROLE.QLHD;
    ROLE_HDDT = ROLE.HDDT;
    ROLE_BAOCAO = ROLE.BaoCao;
    arrAuthorities: any[];

    isRoleTMNH: boolean;
    isRoleMH: boolean;
    isRoleBH: boolean;
    isRoleKHO: boolean;
    isRoleTH: boolean;
    isRoleQLHD: boolean;
    isRoleHDDT: boolean;
    isRoleBC: boolean;
    isCheckUseHDDT: boolean;

    /*Biểu đồ doanh thu chi phí*/
    timeLineVoucherBDDTCP: any;
    fromDateBDDTCP: Moment;
    toDateBDDTCP: Moment;
    chartTypeBDDTCP = 'bar';

    dataBDDTCP: any[];
    public chartDatasetsBDDTCP: ChartDataSets[] = [{ data: [], label: '' }, { data: [], label: '' }];

    public chartLabelsBDDTCP: any[];

    public chartColorsBDDTCP: Array<any> = [
        {
            backgroundColor: [
                '#1AC13A',
                '#1AC13A',
                '#1AC13A',
                '#1AC13A',
                '#1AC13A',
                '#1AC13A',
                '#1AC13A',
                '#1AC13A',
                '#1AC13A',
                '#1AC13A',
                '#1AC13A',
                '#1AC13A'
            ],
            borderColor: [
                '#1AC13A',
                '#1AC13A',
                '#1AC13A',
                '#1AC13A',
                '#1AC13A',
                '#1AC13A',
                '#1AC13A',
                '#1AC13A',
                '#1AC13A',
                '#1AC13A',
                '#1AC13A',
                '#1AC13A'
            ],
            borderWidth: 2
        },
        {
            backgroundColor: [
                '#F9AA1B',
                '#F9AA1B',
                '#F9AA1B',
                '#F9AA1B',
                '#F9AA1B',
                '#F9AA1B',
                '#F9AA1B',
                '#F9AA1B',
                '#F9AA1B',
                '#F9AA1B',
                '#F9AA1B',
                '#F9AA1B'
            ],
            borderColor: [
                '#F9AA1B',
                '#F9AA1B',
                '#F9AA1B',
                '#F9AA1B',
                '#F9AA1B',
                '#F9AA1B',
                '#F9AA1B',
                '#F9AA1B',
                '#F9AA1B',
                '#F9AA1B',
                '#F9AA1B',
                '#F9AA1B'
            ],
            borderWidth: 2
        }
    ];

    /*Biểu dồ nợ phải thu- nợ phải trả*/
    timeLineVoucherBDNPTPT: any;
    fromDateBDNPTPT: Moment;
    toDateBDNPTPT: Moment;
    chartTypeBDNPTPT = 'doughnut';

    dataBDNPTPT: any[];
    public chartDatasetsBDNPTPT: ChartDataSets[] = [{ data: [], label: '' }];

    public chartLabelsBDNPTPT: any;

    public chartColorsBDNPTPT: Array<any> = [
        {
            backgroundColor: ['#189FB3', '#DE2C3A'],
            hoverBackgroundColor: ['#189FB3', '#DE2C3A'],
            borderWidth: 2
        }
    ];

    chartPoint: ChartPoint;

    public chartOptions: any = {};

    public chartOptionsDTCP: any = {};

    public chartOptions2: any = {};

    private subscribe: any;

    constructor(
        private principal: Principal,
        private loginModalService: LoginModalService,
        private eventManager: JhiEventManager,
        public router: Router,
        public homeService: HomeService,
        private toastr: ToastrService,
        private translate: TranslateService,
        private accountListService: AccountListService,
        public utilsService: UtilsService,
        private modalService: NgbModal,
        private userService: UserService
    ) {
        this.treeAccountList = [];
        this.accountLists = [];
        this.listTHead = [];
        this.listKey = [];
        this.navigateForm = './account-list';
        this.listTHead.push('ebwebApp.accountList.accountNumber');
        this.listTHead.push('ebwebApp.accountList.accountName');
        this.listTHead.push('ebwebApp.accountList.debit');
        this.listTHead.push('ebwebApp.accountList.credit');
        this.listKey.push({ key: 'accountNumber', type: 1 });
        this.listKey.push({ key: 'accountName', type: 1 });
        this.listKey.push({ key: 'closingDebitAmount', type: 1 });
        this.listKey.push({ key: 'closingCreditAmount', type: 1 });
    }

    ngOnInit() {
        this.idTab = 1;
        this.principal.identity().then(account => {
            if (account) {
                this.isCheckUseHDDT = account.systemOption.find(a => a.code === 'TCKHAC_SDTichHopHDDT' && a.data).data === '1';
                this.account = account;
                this.arrAuthorities = account.authorities;
                this.isRoleTMNH = !this.arrAuthorities.includes('ROLE_ADMIN') ? this.arrAuthorities.includes(this.ROLE_TMNH) : true;
                this.isRoleMH = !this.arrAuthorities.includes('ROLE_ADMIN') ? this.arrAuthorities.includes(this.ROLE_MH) : true;
                this.isRoleBH = !this.arrAuthorities.includes('ROLE_ADMIN') ? this.arrAuthorities.includes(this.ROLE_BH) : true;
                this.isRoleKHO = !this.arrAuthorities.includes('ROLE_ADMIN') ? this.arrAuthorities.includes(this.ROLE_KHO) : true;
                this.isRoleTH = !this.arrAuthorities.includes('ROLE_ADMIN') ? this.arrAuthorities.includes(this.ROLE_TH) : true;
                this.isRoleQLHD = !this.arrAuthorities.includes('ROLE_ADMIN') ? this.arrAuthorities.includes(this.ROLE_QLHD) : true;
                this.isRoleHDDT = !this.arrAuthorities.includes('ROLE_ADMIN') ? this.arrAuthorities.includes(this.ROLE_HDDT) : true;
                this.isRoleBC = !this.arrAuthorities.includes('ROLE_ADMIN') ? this.arrAuthorities.includes(this.ROLE_BAOCAO) : true;
                console.log(this.account.ebUserPackage);
                console.log(this.account.ebUserPackage.status);
                if (this.account.ebUserPackage.status === 0) {
                    console.log('a');
                    this.ngbModalRef = this.modalService.open(this.activeAlert, {
                        size: 'lg',
                        backdrop: 'static',
                        keyboard: false
                    });
                }
                this.nganCachHangNghin = this.account.systemOption.find(x => x.code === DDSo_NCachHangNghin && x.data).data;
                this.nganCachHangDonVi = this.account.systemOption.find(x => x.code === DDSo_NCachHangDVi && x.data).data;
                this.DDSo_SoAm = this.account.systemOption.find(x => x.code === DDSo_SoAm && x.data).data;
                this.DDSo_TienVND = this.account.systemOption.find(x => x.code === DDSo_TienVND && x.data).data;
                if (this.nganCachHangNghin === ',' && this.DDSo_SoAm === '0') {
                    this.chartOptions = {
                        responsive: true,
                        scales: {
                            yAxes: [
                                {
                                    ticks: {
                                        fontSize: 10,
                                        fontColor: 'red',
                                        beginAtZero: true,
                                        callback(value) {
                                            if (parseInt(value, 10) >= 1000 || parseInt(value, 10) <= -1000) {
                                                const parts = value.toString().split('.');
                                                parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ',');
                                                const valueCopy = parts.join('.');
                                                if (valueCopy.toString().includes('-')) {
                                                    return '(' + valueCopy.replace('-', '') + ')';
                                                } else {
                                                    return valueCopy;
                                                }
                                            } else {
                                                if (value.toString().includes('-')) {
                                                    return '(' + value.replace('-', '') + ')';
                                                } else {
                                                    return value;
                                                }
                                            }
                                        }
                                    }
                                }
                            ],
                            xAxes: [
                                {
                                    ticks: {
                                        autoSkip: false
                                    }
                                }
                            ]
                        },
                        tooltips: {
                            enabled: true,
                            fontSize: 10,
                            mode: 'x-axis',
                            callbacks: {
                                label(tooltipItem, data) {
                                    const label: Label = data.labels[tooltipItem.index];
                                    const datasetLabel = data.datasets[tooltipItem.datasetIndex].data[tooltipItem.index];
                                    if (parseInt(datasetLabel, 10) >= 1000 || parseInt(datasetLabel, 10) <= -1000) {
                                        const parts = datasetLabel.toString().split('.');
                                        parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ',');
                                        const valueCopy = parts.join('.');
                                        if (valueCopy.toString().includes('-')) {
                                            return '(' + valueCopy.replace('-', '') + ')';
                                        } else {
                                            return valueCopy;
                                        }
                                    } else {
                                        if (datasetLabel.toString().includes('-')) {
                                            return '(' + datasetLabel.replace('-', '') + ')';
                                        } else {
                                            return datasetLabel;
                                        }
                                    }
                                }
                            }
                        },
                        /*plugins: {
                            datalabels: {
                                anchor: 'end',
                                align: 'end',
                                font: {
                                    size: 10
                                },
                                padding: 0,
                                formatter(value) {
                                    if (parseInt(value) >= 1000 || parseInt(value) <= -1000) {
                                        const parts = value.toString().split('.');
                                        parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ',');
                                        const valueCopy = parts.join('.');
                                        if (valueCopy.toString().includes('-')) {
                                            return '(' + valueCopy.replace('-', '') + ')';
                                        } else {
                                            return valueCopy;
                                        }
                                    } else {
                                        if (value.toString().includes('-')) {
                                            return '(' + value.replace('-', '') + ')';
                                        } else {
                                            return value;
                                        }
                                    }
                                }
                            }
                        }*/
                        plugins: {
                            datalabels: {
                                display: false
                            }
                        }
                    };
                    this.chartOptions2 = {
                        responsive: true,
                        tooltips: {
                            enabled: true,
                            fontSize: 10,
                            callbacks: {
                                label(tooltipItem, data) {
                                    const label: Label = data.labels[tooltipItem.index];
                                    const datasetLabel = data.datasets[tooltipItem.datasetIndex].data[tooltipItem.index];
                                    if (parseInt(datasetLabel, 10) >= 1000 || parseInt(datasetLabel, 10) <= -1000) {
                                        const parts = datasetLabel.toString().split('.');
                                        parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ',');
                                        const valueCopy = parts.join('.');
                                        if (valueCopy.toString().includes('-')) {
                                            return '(' + valueCopy.replace('-', '') + ')';
                                        } else {
                                            return valueCopy;
                                        }
                                    } else {
                                        if (datasetLabel.toString().includes('-')) {
                                            return '(' + datasetLabel.replace('-', '') + ')';
                                        } else {
                                            return datasetLabel;
                                        }
                                    }
                                }
                            }
                        },
                        plugins: {
                            datalabels: {
                                display: false
                            }
                        }
                        /*plugins: {
                            datalabels: {
                                anchor: 'end',
                                // align: 'end',
                                font: {
                                    size: 10
                                },
                                padding: 0,
                                formatter(value) {
                                    if (parseInt(value) >= 1000 || parseInt(value) <= -1000) {
                                        const parts = value.toString().split('.');
                                        parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ',');
                                        const valueCopy = parts.join('.');
                                        if (valueCopy.toString().includes('-')) {
                                            return '(' + valueCopy.replace('-', '') + ')';
                                        } else {
                                            return valueCopy;
                                        }
                                    } else {
                                        if (value.toString().includes('-')) {
                                            return '(' + value.replace('-', '') + ')';
                                        } else {
                                            return value;
                                        }
                                    }
                                }
                            }
                        }*/
                    };
                    this.chartOptionsDTCP = {
                        responsive: true,
                        scales: {
                            yAxes: [
                                {
                                    ticks: {
                                        fontSize: 10,
                                        fontColor: 'red',
                                        beginAtZero: true,
                                        callback(value) {
                                            if (parseInt(value, 10) >= 1000 || parseInt(value, 10) <= -1000) {
                                                const parts = value.toString().split('.');
                                                parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ',');
                                                const valueCopy = parts.join('.');
                                                if (valueCopy.toString().includes('-')) {
                                                    return '(' + valueCopy.replace('-', '') + ')';
                                                } else {
                                                    return valueCopy;
                                                }
                                            } else {
                                                if (value.toString().includes('-')) {
                                                    return '(' + value.replace('-', '') + ')';
                                                } else {
                                                    return value;
                                                }
                                            }
                                        }
                                    }
                                }
                            ],
                            xAxes: [
                                {
                                    ticks: {
                                        autoSkip: false
                                    }
                                }
                            ]
                        },
                        tooltips: {
                            enabled: true,
                            fontSize: 10,
                            mode: 'x-axis',
                            callbacks: {
                                label(tooltipItem, data) {
                                    const label: Label = data.labels[tooltipItem.index];
                                    const datasetLabel = data.datasets[tooltipItem.datasetIndex].data[tooltipItem.index];
                                    if (parseInt(datasetLabel, 10) >= 1000 || parseInt(datasetLabel, 10) <= -1000) {
                                        const parts = datasetLabel.toString().split('.');
                                        parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ',');
                                        const valueCopy = parts.join('.');
                                        if (valueCopy.toString().includes('-')) {
                                            return '(' + valueCopy.replace('-', '') + ')';
                                        } else {
                                            return valueCopy;
                                        }
                                    } else {
                                        if (datasetLabel.toString().includes('-')) {
                                            return '(' + datasetLabel.replace('-', '') + ')';
                                        } else {
                                            return datasetLabel;
                                        }
                                    }
                                }
                            }
                        },
                        plugins: {
                            datalabels: {
                                display: false
                            }
                        }
                    };
                } else if (this.nganCachHangNghin === ',' && this.DDSo_SoAm === '1') {
                    this.chartOptions = {
                        responsive: true,
                        scales: {
                            yAxes: [
                                {
                                    ticks: {
                                        fontSize: 10,
                                        fontColor: 'red',
                                        beginAtZero: true,
                                        callback(value) {
                                            if (parseInt(value, 10) >= 1000 || parseInt(value, 10) <= -1000) {
                                                const valueCopy = value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
                                                return valueCopy;
                                            } else {
                                                return value;
                                            }
                                        }
                                    }
                                }
                            ],
                            xAxes: [
                                {
                                    ticks: {
                                        autoSkip: false
                                    }
                                }
                            ]
                        },
                        tooltips: {
                            enabled: true,
                            fontSize: 10,
                            mode: 'x-axis',
                            callbacks: {
                                label(tooltipItem, data) {
                                    const label: Label = data.labels[tooltipItem.index];
                                    // data.labels[0] = 'masclmascl';
                                    const datasetLabel = data.datasets[tooltipItem.datasetIndex].data[tooltipItem.index];
                                    if (parseInt(datasetLabel, 10) >= 1000 || parseInt(datasetLabel, 10) <= -1000) {
                                        const parts = datasetLabel.toString().split('.');
                                        parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ',');
                                        const valueCopy = parts.join('.');
                                        return valueCopy;
                                    } else {
                                        return datasetLabel;
                                    }
                                }
                            }
                        },
                        /*plugins: {
                            datalabels: {
                                anchor: 'end',
                                align: 'end',
                                font: {
                                    size: 10
                                },
                                padding: 0,
                                formatter(value) {
                                    if (parseInt(value) >= 1000 || parseInt(value) <= -1000) {
                                        const parts = value.toString().split('.');
                                        parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ',');
                                        const valueCopy = parts.join('.');
                                        return valueCopy;
                                    } else {
                                        return value;
                                    }
                                }
                            }
                        }*/
                        plugins: {
                            datalabels: {
                                display: false
                            }
                        }
                    };
                    this.chartOptions2 = {
                        responsive: true,
                        tooltips: {
                            enabled: true,
                            fontSize: 10,
                            callbacks: {
                                label(tooltipItem, data) {
                                    const label: Label = data.labels[tooltipItem.index];
                                    // data.labels[0] = 'masclmascl';
                                    const datasetLabel = data.datasets[tooltipItem.datasetIndex].data[tooltipItem.index];
                                    if (parseInt(datasetLabel, 10) >= 1000 || parseInt(datasetLabel, 10) <= -1000) {
                                        const parts = datasetLabel.toString().split('.');
                                        parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ',');
                                        const valueCopy = parts.join('.');
                                        return valueCopy;
                                    } else {
                                        return datasetLabel;
                                    }
                                }
                            }
                        },
                        plugins: {
                            datalabels: {
                                display: false
                            }
                        }
                        /*plugins: {
                            datalabels: {
                                anchor: 'end',
                                // align: 'end',
                                font: {
                                    size: 10
                                },
                                padding: 0,
                                formatter(value) {
                                    if (parseInt(value) >= 1000 || parseInt(value) <= -1000) {
                                        const parts = value.toString().split('.');
                                        parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ',');
                                        const valueCopy = parts.join('.');
                                        return valueCopy;
                                    } else {
                                        return value;
                                    }
                                }
                            }
                        }*/
                    };
                    this.chartOptionsDTCP = {
                        responsive: true,
                        scales: {
                            yAxes: [
                                {
                                    ticks: {
                                        fontSize: 10,
                                        fontColor: 'red',
                                        beginAtZero: true,
                                        callback(value) {
                                            if (parseInt(value, 10) >= 1000 || parseInt(value, 10) <= -1000) {
                                                const valueCopy = value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
                                                return valueCopy;
                                            } else {
                                                return value;
                                            }
                                        }
                                    }
                                }
                            ],
                            xAxes: [
                                {
                                    ticks: {
                                        autoSkip: false
                                    }
                                }
                            ]
                        },
                        tooltips: {
                            enabled: true,
                            fontSize: 10,
                            mode: 'x-axis',
                            callbacks: {
                                label(tooltipItem, data) {
                                    const label: Label = data.labels[tooltipItem.index];
                                    // data.labels[0] = 'masclmascl';
                                    const datasetLabel = data.datasets[tooltipItem.datasetIndex].data[tooltipItem.index];
                                    if (parseInt(datasetLabel, 10) >= 1000 || parseInt(datasetLabel, 10) <= -1000) {
                                        const parts = datasetLabel.toString().split('.');
                                        parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ',');
                                        const valueCopy = parts.join('.');
                                        return valueCopy;
                                    } else {
                                        return datasetLabel;
                                    }
                                }
                            }
                        },
                        plugins: {
                            datalabels: {
                                display: false
                            }
                        }
                    };
                } else if (this.nganCachHangNghin === '.' && this.DDSo_SoAm === '0') {
                    this.chartOptions = {
                        responsive: true,
                        scales: {
                            yAxes: [
                                {
                                    ticks: {
                                        fontSize: 10,
                                        fontColor: 'red',
                                        beginAtZero: true,
                                        callback(value) {
                                            if (parseInt(value, 10) >= 1000 || parseInt(value, 10) <= -1000) {
                                                const parts = value.toString().split('.');
                                                parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, '.');
                                                const valueCopy = parts.join(',');
                                                if (valueCopy.toString().includes('-')) {
                                                    return '(' + valueCopy.replace('-', '') + ')';
                                                } else {
                                                    return valueCopy;
                                                }
                                            } else {
                                                if (value.toString().includes('-')) {
                                                    return '(' + value.replace('-', '') + ')';
                                                } else {
                                                    return value;
                                                }
                                            }
                                        }
                                    }
                                }
                            ],
                            xAxes: [
                                {
                                    ticks: {
                                        autoSkip: false
                                    }
                                }
                            ]
                        },
                        tooltips: {
                            enabled: true,
                            fontSize: 10,
                            mode: 'x-axis',
                            callbacks: {
                                label(tooltipItem, data) {
                                    /*const nganCachHangNghin = this.account.systemOption.find(x => x.code === DDSo_NCachHangNghin && x.data).data;
                                    const nganCachHangDonVi = this.account.systemOption.find(x => x.code === DDSo_NCachHangDVi && x.data).data;*/
                                    const label: Label = data.labels[tooltipItem.index];
                                    const datasetLabel = data.datasets[tooltipItem.datasetIndex].data[tooltipItem.index];
                                    if (parseInt(datasetLabel, 10) >= 1000 || parseInt(datasetLabel, 10) <= -1000) {
                                        const parts = datasetLabel.toString().split('.');
                                        parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, '.');
                                        const valueCopy = parts.join(',');
                                        if (valueCopy.toString().includes('-')) {
                                            return '(' + valueCopy.replace('-', '') + ')';
                                        } else {
                                            return valueCopy;
                                        }
                                    } else {
                                        if (datasetLabel.toString().includes('-')) {
                                            return '(' + datasetLabel.replace('-', '') + ')';
                                        } else {
                                            return datasetLabel;
                                        }
                                    }
                                }
                            }
                        },
                        /*plugins: {
                            datalabels: {
                                anchor: 'end',
                                align: 'end',
                                font: {
                                    size: 10
                                },
                                padding: 0,
                                formatter(value) {
                                    if (parseInt(value) >= 1000 || parseInt(value) <= -1000) {
                                        const parts = value.toString().split('.');
                                        parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, '.');
                                        const valueCopy = parts.join(',');
                                        if (valueCopy.toString().includes('-')) {
                                            return '(' + valueCopy.replace('-', '') + ')';
                                        } else {
                                            return valueCopy;
                                        }
                                    } else {
                                        if (value.toString().includes('-')) {
                                            return '(' + value.replace('-', '') + ')';
                                        } else {
                                            return value;
                                        }
                                    }
                                }
                            }
                        }*/
                        plugins: {
                            datalabels: {
                                display: false
                            }
                        }
                    };
                    this.chartOptions2 = {
                        responsive: true,
                        tooltips: {
                            enabled: true,
                            fontSize: 10,
                            callbacks: {
                                label(tooltipItem, data) {
                                    /*const nganCachHangNghin = this.account.systemOption.find(x => x.code === DDSo_NCachHangNghin && x.data).data;
                                    const nganCachHangDonVi = this.account.systemOption.find(x => x.code === DDSo_NCachHangDVi && x.data).data;*/
                                    const label: Label = data.labels[tooltipItem.index];
                                    const datasetLabel = data.datasets[tooltipItem.datasetIndex].data[tooltipItem.index];
                                    if (parseInt(datasetLabel, 10) >= 1000 || parseInt(datasetLabel, 10) <= -1000) {
                                        const parts = datasetLabel.toString().split('.');
                                        parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, '.');
                                        const valueCopy = parts.join(',');
                                        if (valueCopy.toString().includes('-')) {
                                            return '(' + valueCopy.replace('-', '') + ')';
                                        } else {
                                            return valueCopy;
                                        }
                                    } else {
                                        if (datasetLabel.toString().includes('-')) {
                                            return '(' + datasetLabel.replace('-', '') + ')';
                                        } else {
                                            return datasetLabel;
                                        }
                                    }
                                }
                            }
                        },
                        plugins: {
                            datalabels: {
                                display: false
                            }
                        }
                        /*plugins: {
                            datalabels: {
                                anchor: 'end',
                                /!*align: function(context) {
                                    return context.dataset.data[context.dataIndex] < 15 ? 'center' : 'end';
                                },*!/
                                font: {
                                    size: 10
                                },
                                padding: 0,
                                formatter(value) {
                                    if (parseInt(value) >= 1000 || parseInt(value) <= -1000) {
                                        const parts = value.toString().split('.');
                                        parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, '.');
                                        const valueCopy = parts.join(',');
                                        if (valueCopy.toString().includes('-')) {
                                            return '(' + valueCopy.replace('-', '') + ')';
                                        } else {
                                            return valueCopy;
                                        }
                                    } else {
                                        if (value.toString().includes('-')) {
                                            return '(' + value.replace('-', '') + ')';
                                        } else {
                                            return value;
                                        }
                                    }
                                }
                            }
                        }*/
                    };
                    this.chartOptionsDTCP = {
                        responsive: true,
                        scales: {
                            yAxes: [
                                {
                                    ticks: {
                                        fontSize: 10,
                                        fontColor: 'red',
                                        beginAtZero: true,
                                        callback(value) {
                                            if (parseInt(value, 10) >= 1000 || parseInt(value, 10) <= -1000) {
                                                const parts = value.toString().split('.');
                                                parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, '.');
                                                const valueCopy = parts.join(',');
                                                if (valueCopy.toString().includes('-')) {
                                                    return '(' + valueCopy.replace('-', '') + ')';
                                                } else {
                                                    return valueCopy;
                                                }
                                            } else {
                                                if (value.toString().includes('-')) {
                                                    return '(' + value.replace('-', '') + ')';
                                                } else {
                                                    return value;
                                                }
                                            }
                                        }
                                    }
                                }
                            ],
                            xAxes: [
                                {
                                    ticks: {
                                        autoSkip: false
                                    }
                                }
                            ]
                        },
                        tooltips: {
                            enabled: true,
                            fontSize: 10,
                            mode: 'x-axis',
                            callbacks: {
                                label(tooltipItem, data) {
                                    /*const nganCachHangNghin = this.account.systemOption.find(x => x.code === DDSo_NCachHangNghin && x.data).data;
                                    const nganCachHangDonVi = this.account.systemOption.find(x => x.code === DDSo_NCachHangDVi && x.data).data;*/
                                    const label: Label = data.labels[tooltipItem.index];
                                    const datasetLabel = data.datasets[tooltipItem.datasetIndex].data[tooltipItem.index];
                                    if (parseInt(datasetLabel, 10) >= 1000 || parseInt(datasetLabel, 10) <= -1000) {
                                        const parts = datasetLabel.toString().split('.');
                                        parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, '.');
                                        const valueCopy = parts.join(',');
                                        if (valueCopy.toString().includes('-')) {
                                            return '(' + valueCopy.replace('-', '') + ')';
                                        } else {
                                            return valueCopy;
                                        }
                                    } else {
                                        if (datasetLabel.toString().includes('-')) {
                                            return '(' + datasetLabel.replace('-', '') + ')';
                                        } else {
                                            return datasetLabel;
                                        }
                                    }
                                }
                            }
                        },
                        plugins: {
                            datalabels: {
                                display: false
                            }
                        }
                    };
                } else if (this.nganCachHangNghin === '.' && this.DDSo_SoAm === '1') {
                    this.chartOptions = {
                        responsive: true,
                        scales: {
                            yAxes: [
                                {
                                    ticks: {
                                        fontSize: 10,
                                        fontColor: 'red',
                                        beginAtZero: true,
                                        callback(value) {
                                            if (parseInt(value, 10) >= 1000 || parseInt(value, 10) <= -1000) {
                                                const parts = value.toString().split('.');
                                                parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, '.');
                                                const valueCopy = parts.join(',');
                                                return valueCopy;
                                            } else {
                                                return value;
                                            }
                                        }
                                    }
                                }
                            ],
                            xAxes: [
                                {
                                    ticks: {
                                        autoSkip: false
                                    }
                                }
                            ]
                        },
                        tooltips: {
                            enabled: true,
                            fontSize: 10,
                            mode: 'x-axis',
                            callbacks: {
                                label(tooltipItem, data) {
                                    /*const nganCachHangNghin = this.account.systemOption.find(x => x.code === DDSo_NCachHangNghin && x.data).data;
                                    const nganCachHangDonVi = this.account.systemOption.find(x => x.code === DDSo_NCachHangDVi && x.data).data;*/
                                    const label: Label = data.labels[tooltipItem.index];
                                    const datasetLabel = data.datasets[tooltipItem.datasetIndex].data[tooltipItem.index];
                                    if (parseInt(datasetLabel, 10) >= 1000 || parseInt(datasetLabel, 10) <= -1000) {
                                        const parts = datasetLabel.toString().split('.');
                                        parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, '.');
                                        const valueCopy = parts.join(',');
                                        return valueCopy;
                                    } else {
                                        return datasetLabel;
                                    }
                                }
                            }
                        },
                        /*plugins: {
                            datalabels: {
                                anchor: 'end',
                                align: 'end',
                                font: {
                                    size: 10
                                },
                                padding: 0,
                                formatter(value) {
                                    if (parseInt(value) >= 1000 || parseInt(value) <= -1000) {
                                        const parts = value.toString().split('.');
                                        parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, '.');
                                        const valueCopy = parts.join(',');
                                        return valueCopy;
                                    } else {
                                        return value;
                                    }
                                }
                            }
                        }*/
                        plugins: {
                            datalabels: {
                                display: false
                            }
                        }
                    };
                    this.chartOptions2 = {
                        responsive: true,
                        tooltips: {
                            enabled: true,
                            fontSize: 10,
                            callbacks: {
                                label(tooltipItem, data) {
                                    /*const nganCachHangNghin = this.account.systemOption.find(x => x.code === DDSo_NCachHangNghin && x.data).data;
                                    const nganCachHangDonVi = this.account.systemOption.find(x => x.code === DDSo_NCachHangDVi && x.data).data;*/
                                    const label: Label = data.labels[tooltipItem.index];
                                    const datasetLabel = data.datasets[tooltipItem.datasetIndex].data[tooltipItem.index];
                                    if (parseInt(datasetLabel, 10) >= 1000 || parseInt(datasetLabel, 10) <= -1000) {
                                        const parts = datasetLabel.toString().split('.');
                                        parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, '.');
                                        const valueCopy = parts.join(',');
                                        return valueCopy;
                                    } else {
                                        return datasetLabel;
                                    }
                                }
                            }
                        },
                        plugins: {
                            datalabels: {
                                display: false
                            }
                        }
                        /*plugins: {
                            datalabels: {
                                anchor: 'end',
                                // align: 'end',
                                font: {
                                    size: 10
                                },
                                padding: 0,
                                formatter(value) {
                                    if (parseInt(value) >= 1000 || parseInt(value) <= -1000) {
                                        const parts = value.toString().split('.');
                                        parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, '.');
                                        const valueCopy = parts.join(',');
                                        return valueCopy;
                                    } else {
                                        return value;
                                    }
                                }
                            }
                        }*/
                    };
                    this.chartOptionsDTCP = {
                        responsive: true,
                        scales: {
                            yAxes: [
                                {
                                    ticks: {
                                        fontSize: 10,
                                        fontColor: 'red',
                                        beginAtZero: true,
                                        callback(value) {
                                            if (parseInt(value, 10) >= 1000 || parseInt(value, 10) <= -1000) {
                                                const parts = value.toString().split('.');
                                                parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, '.');
                                                const valueCopy = parts.join(',');
                                                return valueCopy;
                                            } else {
                                                return value;
                                            }
                                        }
                                    }
                                }
                            ],
                            xAxes: [
                                {
                                    ticks: {
                                        autoSkip: false
                                    }
                                }
                            ]
                        },
                        tooltips: {
                            enabled: true,
                            fontSize: 10,
                            mode: 'x-axis',
                            callbacks: {
                                label(tooltipItem, data) {
                                    /*const nganCachHangNghin = this.account.systemOption.find(x => x.code === DDSo_NCachHangNghin && x.data).data;
                                    const nganCachHangDonVi = this.account.systemOption.find(x => x.code === DDSo_NCachHangDVi && x.data).data;*/
                                    const label: Label = data.labels[tooltipItem.index];
                                    const datasetLabel = data.datasets[tooltipItem.datasetIndex].data[tooltipItem.index];
                                    if (parseInt(datasetLabel, 10) >= 1000 || parseInt(datasetLabel, 10) <= -1000) {
                                        const parts = datasetLabel.toString().split('.');
                                        parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, '.');
                                        const valueCopy = parts.join(',');
                                        return valueCopy;
                                    } else {
                                        return datasetLabel;
                                    }
                                }
                            }
                        },
                        plugins: {
                            datalabels: {
                                display: false
                            }
                        }
                    };
                }
                this.utilsService.getCbbTimeLine2().then(data => {
                    this.listTimeLine = data;
                    this.timeLineVoucherSKTC = this.listTimeLine[3].value;
                    this.timeLineVoucherBDTH = this.listTimeLine[7].value;
                    this.timeLineVoucherBDDTCP = this.listTimeLine[5].value;
                    this.timeLineVoucherBDNPTPT = this.listTimeLine[7].value;
                    this.selectChangeBeginDateAndEndDateSKTC(this.timeLineVoucherSKTC);
                    this.selectChangeBeginDateAndEndDateBDTH(this.timeLineVoucherSKTC);
                    this.selectChangeBeginDateAndEndDateBDDTCP(this.timeLineVoucherBDDTCP, true);
                    this.selectChangeBeginDateAndEndDateBDPTPT(this.timeLineVoucherBDNPTPT);
                    this.getSoDuTaiKhoan();
                });
            } else {
                if (this.router.url.includes('/admin/login')) {
                    this.router.navigate(['/admin/login']);
                } else {
                    this.router.navigate(['/login']);
                }
            }
        });
        this.registerAuthenticationSuccess();
        this.registerSelectedRow();
        this.registerChangeSession();
    }

    setOption(option, nganCachHN, nganCachHangDV) {
        option = {
            responsive: true,
            scales: {
                yAxes: [
                    {
                        ticks: {
                            fontSize: 10,
                            fontColor: 'red',
                            beginAtZero: true,
                            callback(value) {
                                if (parseInt(value, 10) >= 1000 || parseInt(value, 10) <= -1000) {
                                    const valueCopy = value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, 'nganCachHN');
                                    if (valueCopy.toString().includes('-')) {
                                        return '(' + valueCopy.replace('-', '') + ')';
                                    } else {
                                        return valueCopy;
                                    }
                                } else {
                                    if (value.toString().includes('-')) {
                                        return '(' + value.replace('-', '') + ')';
                                    } else {
                                        return value;
                                    }
                                }
                            }
                        }
                    }
                ]
            },
            tooltips: {
                enabled: true,
                fontSize: 10,
                mode: 'x-axis',
                callbacks: {
                    label(tooltipItem, data) {
                        /*const nganCachHangNghin = this.account.systemOption.find(x => x.code === DDSo_NCachHangNghin && x.data).data;
                        const nganCachHangDonVi = this.account.systemOption.find(x => x.code === DDSo_NCachHangDVi && x.data).data;*/
                        const label: Label = data.labels[tooltipItem.index];
                        const datasetLabel = data.datasets[tooltipItem.datasetIndex].data[tooltipItem.index];
                        if (parseInt(datasetLabel, 10) >= 1000 || parseInt(datasetLabel, 10) <= -1000) {
                            const valueCopy = datasetLabel.toString().replace(/\B(?=(\d{3})+(?!\d))/g, '.');
                            if (valueCopy.toString().includes('-')) {
                                return '(' + valueCopy.replace('-', '') + ')';
                            } else {
                                return valueCopy;
                            }
                        } else {
                            if (datasetLabel.toString().includes('-')) {
                                return '(' + datasetLabel.replace('-', '') + ')';
                            } else {
                                return datasetLabel;
                            }
                        }
                    }
                }
            },
            plugins: {
                datalabels: {
                    anchor: 'end',
                    align: 'end',
                    font: {
                        size: 10
                    },
                    padding: 0,
                    formatter(value) {
                        if (parseInt(value, 10) >= 1000 || parseInt(value, 10) <= -1000) {
                            const valueCopy = value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, '.');
                            if (valueCopy.toString().includes('-')) {
                                return '(' + valueCopy.replace('-', '') + ')';
                            } else {
                                return valueCopy;
                            }
                        } else {
                            if (value.toString().includes('-')) {
                                return '(' + value.replace('-', '') + ')';
                            } else {
                                return value;
                            }
                        }
                    }
                }
            }
            /*animation: {
                duration: 1,
                onComplete() {
                    const chartInstance = this.chart,
                        ctx = chartInstance.ctx;
                    ctx.textAlign = 'center';
                    ctx.fillStyle = 'rgba(0, 0, 0, 1)';
                    // ctx.textBaseline = 'bottom';
                    ctx.font = '10px Arial';
                    /!*const nganCachHangNghin = this.account.systemOption.find(x => x.code === DDSo_NCachHangNghin && x.data).data;
                    const nganCachHangDonVi = this.account.systemOption.find(x => x.code === DDSo_NCachHangDVi && x.data).data;*!/
                    this.data.datasets.forEach((dataset, i) => {
                        const meta = chartInstance.controller.getDatasetMeta(i);
                        meta.data.forEach((bar, index) => {
                            const data = dataset.data[index];
                            if (data) {
                                /!*const parts = data.toString().split('.');
                                parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, '.');
                                ctx.fillText(parts.join(','), bar._model.x, bar._model.y - 1);*!/
                                if (parseInt(data) >= 1000 || parseInt(data) <= -1000) {
                                    const valueCopy = data.toString().replace(/\B(?=(\d{3})+(?!\d))/g, '.');
                                    if (valueCopy.toString().includes('-')) {
                                        ctx.fillText('(' + valueCopy.replace('-', '') + ')', bar._model.x, bar._model.y - 1);
                                    } else {
                                        ctx.fillText(valueCopy, bar._model.x, bar._model.y - 1);
                                    }
                                } else {
                                    if (data.toString().includes('-')) {
                                        ctx.fillText('(' + data.replace('-', '') + ')', bar._model.x, bar._model.y - 1);
                                    } else {
                                        ctx.fillText(data, bar._model.x, bar._model.y - 1);
                                    }
                                }
                            }
                        });
                    });
                }
            }*/
        };
    }

    getAllData() {
        this.homeService
            .getAllData({
                fromDate: moment(this.fromDateSKTC, DATE_FORMAT_SEARCH).format(DATE_FORMAT),
                toDate: moment(this.toDateSKTC, DATE_FORMAT_SEARCH).format(DATE_FORMAT)
            })
            .subscribe((res: HttpResponse<any>) => {
                this.sucKhoeTaiChinhs = res.body.sucKhoeTaiChinhDoanhNghiepDTOS;
                this.getSoDuTaiKhoanFromAll(res.body.bangCanDoiTaiKhoanDTOList);
                this.getBieuDoTongHopFromAll(res.body.bieuDoTongHopDTOList);
                this.getBieuDoDoanhThuChiPhiFromAll(res.body.bieuDoDoanhThuChiPhiDTOList);
                this.getBieuDoNoPhaiThuTraFromAll(res.body.bieuDoNoPhaiThuTraList);
            });
    }

    getBieuDoNoPhaiThuTraFromAll(bieuDoNoPhaiThuTraList) {
        this.dataBDNPTPT = [];
        this.chartLabelsBDNPTPT = [];
        (bieuDoNoPhaiThuTraList as any[]).forEach(n => {
            this.dataBDNPTPT.push(n.amount);
            this.chartLabelsBDNPTPT.push(n.name);
        });
        this.chartDatasetsBDNPTPT = [{ data: this.dataBDNPTPT, label: '' }];
    }

    getBieuDoTongHopFromAll(bieuDoTongHopDTOList) {
        this.dataBDTH = [];
        this.chartLabelsBDTH = [',', '.'];
        const labelCheck: any[] = [];
        (bieuDoTongHopDTOList as any[]).forEach(n => {
            this.dataBDTH.push(n.amount);
            labelCheck.push(n.itemName);
        });
        this.chartDatasetsBDTH = [{ data: this.dataBDTH }];
        this.chartLabelsBDTH = labelCheck;
    }

    getBieuDoDoanhThuChiPhiFromAll(bieuDoDoanhThuChiPhiDTOList) {
        const dataDT = [];
        const dataCP = [];
        this.chartLabelsBDDTCP = [];
        (bieuDoDoanhThuChiPhiDTOList as any[]).forEach(n => {
            if (n.name === 'Doanh thu') {
                dataDT.push(n.amount);
            } else {
                dataCP.push(n.amount);
            }
            const fromDate = moment(n.fromDate, DATE_FORMAT);
            const toDate = moment(n.toDate, DATE_FORMAT);
            const diff = toDate.diff(fromDate, 'days');
            if (diff + 1 === fromDate.daysInMonth()) {
                const timeLineString = 'T' + (1 + toDate.month());
                if (!this.chartLabelsBDDTCP.includes(timeLineString)) {
                    this.chartLabelsBDDTCP.push(timeLineString);
                }
                this.showlabel = false;
            } else {
                this.showlabel = true;
                let timeLineString = '';
                if (fromDate.isSame(toDate)) {
                    timeLineString = fromDate.format(DATE_FORMAT_SLASH);
                } else {
                    timeLineString = fromDate.format(DATE_FORMAT_SLASH) + ' - ' + toDate.format(DATE_FORMAT_SLASH);
                }
                if (!this.chartLabelsBDDTCP.includes(timeLineString)) {
                    this.chartLabelsBDDTCP.push(timeLineString);
                }
            }
        });
        this.chartDatasetsBDDTCP = [{ data: dataDT, label: 'Doanh thu' }, { data: dataCP, label: 'Chi phí' }];
    }

    getSoDuTaiKhoanFromAll(accList) {
        this.listParentAccount = [];
        this.flatTreeAccountList = [];
        const body = accList;
        this.accountLists = body;
        this.selectedRow = this.accountLists[0];
        // let index = 0;
        const listAccount = this.accountLists.filter(a => a.grade === 1);
        for (let i = 0; i < listAccount.length; i++) {
            this.listParentAccount.push(Object.assign({}));
            this.listParentAccount[i].parent = listAccount[i];
            // index++;
        }
        this.tree(this.listParentAccount, 1);
        this.cutTree(this.listParentAccount);
        this.selectedRow = this.flatTreeAccountList[0];
    }

    getSoDuTaiKhoan() {
        this.treeAccountList = [];
        this.accountLists = [];
        this.listTHead = [];
        this.listKey = [];
        this.navigateForm = './account-list';
        this.listTHead.push('ebwebApp.accountList.accountNumber');
        this.listTHead.push('ebwebApp.accountList.accountName');
        this.listTHead.push('ebwebApp.accountList.debit');
        this.listTHead.push('ebwebApp.accountList.credit');
        this.listKey.push({ key: 'accountNumber', type: 1 });
        this.listKey.push({ key: 'accountName', type: 1 });
        this.listKey.push({ key: 'closingDebitAmountString', type: 3 });
        this.listKey.push({ key: 'closingCreditAmountString', type: 3 });
        this.homeService.getSoDuTaiKhoan().subscribe((res: HttpResponse<any>) => {
            this.listParentAccount = [];
            this.flatTreeAccountList = [];
            const body = res.body;
            this.accountLists = body;
            this.selectedRow = this.accountLists[0];
            // let index = 0;
            const listAccount = this.accountLists.filter(a => a.grade === 1);
            for (let i = 0; i < listAccount.length; i++) {
                this.listParentAccount.push(Object.assign({}));
                this.listParentAccount[i].parent = listAccount[i];
                // index++;
            }
            this.tree(this.listParentAccount, 1);
            // this.cutTree(this.listParentAccount);
            this.selectedRow = this.flatTreeAccountList[0];
        });
        this.registerSelectedRow();
    }

    getSucKhoeTaiChinh() {
        this.homeService
            .getSucKhoeTaiChinh({
                fromDate: moment(this.fromDateSKTC, DATE_FORMAT_SEARCH).format(DATE_FORMAT),
                toDate: moment(this.toDateSKTC, DATE_FORMAT_SEARCH).format(DATE_FORMAT)
            })
            .subscribe((res: HttpResponse<any>) => {
                this.sucKhoeTaiChinhs = res.body;
            });
    }

    getBieuDoTongHop() {
        this.homeService
            .getBieuDoTongHop({
                fromDate: moment(this.fromDateBDTH, DATE_FORMAT_SEARCH).format(DATE_FORMAT),
                toDate: moment(this.toDateBDTH, DATE_FORMAT_SEARCH).format(DATE_FORMAT)
            })
            .subscribe((res: HttpResponse<any>) => {
                this.getBieuDoTongHopFromAll(res.body);
            });
    }

    getBieuDoDoanhThuChiPhi() {
        this.homeService
            .getBieuDoDoanhThuChiPhi({
                fromDate: moment(this.fromDateBDDTCP, DATE_FORMAT_SEARCH).format(DATE_FORMAT),
                toDate: moment(this.toDateBDDTCP, DATE_FORMAT_SEARCH).format(DATE_FORMAT)
            })
            .subscribe((res: HttpResponse<any>) => {
                this.getBieuDoDoanhThuChiPhiFromAll(res.body);
            });
    }

    getBieuDoNoPhaiThuTra() {
        this.homeService
            .getBieuDoNoPhaiThuTra({
                fromDate: moment(this.fromDateBDNPTPT, DATE_FORMAT_SEARCH).format(DATE_FORMAT),
                toDate: moment(this.toDateBDNPTPT, DATE_FORMAT_SEARCH).format(DATE_FORMAT)
            })
            .subscribe((res: HttpResponse<any>) => {
                this.getBieuDoNoPhaiThuTraFromAll(res.body);
            });
    }

    selectChangeBeginDateAndEndDateSKTC(intTimeLine: String) {
        if (intTimeLine) {
            this.objTimeLine = this.utilsService.getTimeLine(intTimeLine);
            this.fromDateSKTC = moment(this.objTimeLine.dtBeginDate);
            this.toDateSKTC = moment(this.objTimeLine.dtEndDate);
            this.getSucKhoeTaiChinh();
        }
    }

    selectChangeBeginDateAndEndDateBDTH(intTimeLine: String) {
        if (intTimeLine) {
            this.objTimeLine = this.utilsService.getTimeLine(intTimeLine);
            this.fromDateBDTH = moment(this.objTimeLine.dtBeginDate);
            this.toDateBDTH = moment(this.objTimeLine.dtEndDate);
            this.getBieuDoTongHop();
        }
    }

    selectChangeBeginDateAndEndDateBDDTCP(intTimeLine: String, loadOninit?) {
        if (intTimeLine) {
            /*if (loadOninit) {
                const nowD = moment(new Date(), DATE_FORMAT);
                const sub = nowD.month() - 1;
                if (sub < 0) {
                    this.fromDateBDDTCP = moment('01-' + String(sub + 12) + '-' + String(nowD.year() - 1), DATE_FORMAT_TYPE1);
                } else if (sub === 0) {
                    this.fromDateBDDTCP = moment('01-12-' + String(nowD.year() - 1), DATE_FORMAT_TYPE1);
                } else {
                    this.fromDateBDDTCP = moment('01-' + String(sub) + '-' + String(nowD.year()), DATE_FORMAT_TYPE1);
                }
                this.toDateBDDTCP = nowD;
                setTimeout(to => {
                    this.getBieuDoDoanhThuChiPhi();
                }, 200);
            } else {
                this.objTimeLine = this.utilsService.getTimeLine(intTimeLine);
                this.fromDateBDDTCP = moment(this.objTimeLine.dtBeginDate);
                this.toDateBDDTCP = moment(this.objTimeLine.dtEndDate);
                setTimeout(to => {
                    this.getBieuDoDoanhThuChiPhi();
                }, 200);
            }*/
            this.objTimeLine = this.utilsService.getTimeLine(intTimeLine);
            this.fromDateBDDTCP = moment(this.objTimeLine.dtBeginDate);
            this.toDateBDDTCP = moment(this.objTimeLine.dtEndDate);
            this.getBieuDoDoanhThuChiPhi();
        }
    }

    selectChangeBeginDateAndEndDateBDPTPT(intTimeLine: String) {
        if (intTimeLine) {
            this.objTimeLine = this.utilsService.getTimeLine(intTimeLine);
            this.fromDateBDNPTPT = moment(this.objTimeLine.dtBeginDate);
            this.toDateBDNPTPT = moment(this.objTimeLine.dtEndDate);
            this.getBieuDoNoPhaiThuTra();
        }
    }

    registerSelectedRow() {
        this.eventSubscriber = this.eventManager.subscribe('selectRow', response => {
            this.selectedRow = response.data;
        });
    }

    tree(accountList: ITreeAccountList[], grade) {
        for (let i = 0; i < accountList.length; i++) {
            const newList = this.accountLists.filter(a => a.parentAccountID === accountList[i].parent.id);
            accountList[i].children = [];
            for (let j = 0; j < newList.length; j++) {
                accountList[i].children.push(Object.assign({}));
                accountList[i].children[j].parent = newList[j];
            }
            if (accountList[i].children && accountList[i].children.length > 0) {
                this.tree(accountList[i].children, grade + 1);
            }
        }
        for (let i = 0; i < accountList.length; i++) {
            if (accountList[i].parent.accountGroupKind !== null || accountList[i].parent.accountGroupKind !== undefined) {
                accountList[i].parent.getAccountGroupKind = this.getAccountGroupKind(accountList[i].parent.accountGroupKind);
            }
        }
    }

    cutTree(tree: ITreeAccountList[]) {
        tree.forEach(branch => {
            this.flatTreeAccountList.push(branch);
            if (branch.children && branch.children.length > 0) {
                this.cutTree(branch.children);
            }
        });
    }

    getAccountGroupKind(accountGroupKind: number) {
        if (accountGroupKind === 0) {
            return this.translate.instant('ebwebApp.accountList.debit');
        } else if (accountGroupKind === 1) {
            return this.translate.instant('ebwebApp.accountList.credit');
        } else if (accountGroupKind === 2) {
            return this.translate.instant('ebwebApp.accountList.hermaphrodite');
        } else if (accountGroupKind === 3) {
            return this.translate.instant('ebwebApp.accountList.noBalance');
        }
    }

    registerAuthenticationSuccess() {
        this.subscribe = this.eventManager.subscribe('authenticationSuccess', message => {
            this.homeService.getInfoPackage().subscribe(
                (res: HttpResponse<any>) => {
                    this.infoPackage = res.body;
                    this.warningInfoPackage();
                },
                (res: HttpErrorResponse) => {}
            );

            this.homeService.checkDayBackup().subscribe(
                (res: HttpResponse<any>) => {
                    const daysBackup = res.body;
                    if (daysBackup >= 15) {
                        this.warningBackup();
                    }
                },
                (res: HttpErrorResponse) => {}
            );
        });
    }

    warningBackup() {
        this.toastr.warning(this.translate.instant('ebwebApp.dataBackup.warning'));
    }

    warningInfoPackage() {
        if (
            this.infoPackage.warningLimitedQuantity === WarningPackage.limitedNo &&
            this.infoPackage.warningExpiredTime === WarningPackage.expiredTime
        ) {
            this.toastr.warning(this.translate.instant('home.warning.limitedNoAndExpiredTime'));
        } else if (this.infoPackage.warningLimitedQuantity === WarningPackage.limitedNo) {
            this.toastr.warning(this.translate.instant('home.warning.limitedNo'));
        } else if (this.infoPackage.warningExpiredTime === WarningPackage.expiredTime) {
            this.toastr.warning(this.translate.instant('home.warning.expiredTime'));
        }
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    login() {
        this.modalRef = this.loginModalService.open();
    }

    /*Biểu đồ tổng hợp*/

    public chartClicked(e: any): void {}

    public chartHovered(e: any): void {}

    numberWithCommas(x) {
        if (x.toString().length > 3) {
            const parts = x.toString().split('.');
            parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ',');
            return parts.join('.');
        } else {
            return x;
        }
    }

    registerChangeSession() {
        this.eventManager.subscribe('changeSession', response => {
            this.principal.identity().then(account => {
                if (account) {
                    this.account = account;
                    this.listTimeLine = this.utilsService.getCbbTimeLine2().then(() => {
                        this.timeLineVoucherSKTC = this.listTimeLine[3].value;
                        this.timeLineVoucherBDTH = this.listTimeLine[3].value;
                        this.timeLineVoucherBDDTCP = this.listTimeLine[3].value;
                        this.timeLineVoucherBDNPTPT = this.listTimeLine[3].value;
                        this.selectChangeBeginDateAndEndDateSKTC(this.timeLineVoucherSKTC);
                        this.selectChangeBeginDateAndEndDateBDTH(this.timeLineVoucherSKTC);
                        this.selectChangeBeginDateAndEndDateBDDTCP(this.timeLineVoucherSKTC);
                        this.selectChangeBeginDateAndEndDateBDPTPT(this.timeLineVoucherSKTC);
                        this.getSoDuTaiKhoan();
                        // this.getAllData();
                    });
                } else {
                    if (this.router.url.includes('/admin/login')) {
                        this.router.navigate(['/admin/login']);
                    } else {
                        this.router.navigate(['/login']);
                    }
                }
            });
        });
    }

    navigateData() {
        this.router.navigate(['data-backup']);
    }

    navigateTienMatNganHang() {
        if (this.isRoleTMNH) {
            this.router.navigate(['quy-trinh-tien-mat-ngan-hang']);
        } else {
            this.toastr.error(this.translate.instant('ebwebApp.quyTrinh.notHasAccess'));
        }
    }

    navigateMuaHang() {
        if (this.isRoleMH) {
            this.router.navigate(['quy-trinh-mua-hang']);
        } else {
            this.toastr.error(this.translate.instant('ebwebApp.quyTrinh.notHasAccess'));
        }
    }

    navigateBanHang() {
        if (this.isRoleBH) {
            this.router.navigate(['quy-trinh-ban-hang']);
        } else {
            this.toastr.error(this.translate.instant('ebwebApp.quyTrinh.notHasAccess'));
        }
    }

    navigateQuanLyHoaDon() {
        if (this.isRoleQLHD) {
            this.router.navigate(['quy-trinh-quan-ly-hoa-don']);
        } else {
            this.toastr.error(this.translate.instant('ebwebApp.quyTrinh.notHasAccess'));
        }
    }

    navigateHoaDonDienTu() {
        if (this.isRoleHDDT && this.isCheckUseHDDT) {
            this.router.navigate(['quy-trinh-hoa-don-dien-tu']);
        } else {
            this.toastr.error(this.translate.instant('ebwebApp.quyTrinh.notHasAccess'));
        }
    }

    navigateKho() {
        if (this.isRoleKHO) {
            this.router.navigate(['quy-trinh-kho']);
        } else {
            this.toastr.error(this.translate.instant('ebwebApp.quyTrinh.notHasAccess'));
        }
    }

    navigateTongHop() {
        if (this.isRoleTH) {
            this.router.navigate(['quy-trinh-tong-hop']);
        } else {
            this.toastr.error(this.translate.instant('ebwebApp.quyTrinh.notHasAccess'));
        }
    }

    navigateBaoCao() {
        if (this.isRoleBC) {
            this.router.navigate(['bao-cao']);
        } else {
            this.toastr.error(this.translate.instant('ebwebApp.quyTrinh.notHasAccess'));
        }
    }

    activeAccount() {
        this.account.ebUserPackage.status = 1;
        this.userService.activePackage(this.account.ebUserPackage).subscribe(
            response => {
                console.log(this.account.ebUserPackage.status);
                this.toastr.success(this.translate.instant('home.activeSuccess'));
            },
            error => {
                this.toastr.error(this.translate.instant('home.activeFail'));
            }
        );
        this.ngbModalRef.close();
    }

    ngOnDestroy(): void {
        console.log('subscribe');
        this.eventManager.destroy(this.subscribe);
    }
}
