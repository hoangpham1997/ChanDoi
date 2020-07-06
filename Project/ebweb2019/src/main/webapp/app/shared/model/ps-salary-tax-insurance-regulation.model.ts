import { Moment } from 'moment';

export interface IPSSalaryTaxInsuranceRegulation {
    id?: number;
    fromDate?: Moment;
    toDate?: Moment;
    basicWage?: number;
    insuaranceMaximumizeSalary?: number;
    reduceSelfTaxAmount?: number;
    reduceDependTaxAmount?: number;
    workHoursInDay?: number;
    isWorkingOnMSaturday?: boolean;
    isWorkingOnMSunday?: boolean;
    isWorkingOnNSaturday?: boolean;
    isWorkingOnNSunday?: boolean;
    overtimeDailyPercent?: number;
    overtimeWeekendPercent?: number;
    overtimeHolidayPercent?: number;
    overtimeWorkingDayNightPercent?: number;
    overtimeWeekendDayNightPercent?: number;
    overtimeHolidayNightPercent?: number;
    companySocityInsurancePercent?: number;
    companytAccidentInsurancePercent?: number;
    companyMedicalInsurancePercent?: number;
    companyUnEmployeeInsurancePercent?: number;
    companyTradeUnionInsurancePercent?: number;
    employeeSocityInsurancePercent?: number;
    employeeAccidentInsurancePercent?: number;
    employeeMedicalInsurancePercent?: number;
    employeeUnEmployeeInsurancePercent?: number;
    employeeTradeUnionInsurancePercent?: number;
}

export class PSSalaryTaxInsuranceRegulation implements IPSSalaryTaxInsuranceRegulation {
    constructor(
        public id?: number,
        public fromDate?: Moment,
        public toDate?: Moment,
        public basicWage?: number,
        public insuaranceMaximumizeSalary?: number,
        public reduceSelfTaxAmount?: number,
        public reduceDependTaxAmount?: number,
        public workHoursInDay?: number,
        public isWorkingOnMSaturday?: boolean,
        public isWorkingOnMSunday?: boolean,
        public isWorkingOnNSaturday?: boolean,
        public isWorkingOnNSunday?: boolean,
        public overtimeDailyPercent?: number,
        public overtimeWeekendPercent?: number,
        public overtimeHolidayPercent?: number,
        public overtimeWorkingDayNightPercent?: number,
        public overtimeWeekendDayNightPercent?: number,
        public overtimeHolidayNightPercent?: number,
        public companySocityInsurancePercent?: number,
        public companytAccidentInsurancePercent?: number,
        public companyMedicalInsurancePercent?: number,
        public companyUnEmployeeInsurancePercent?: number,
        public companyTradeUnionInsurancePercent?: number,
        public employeeSocityInsurancePercent?: number,
        public employeeAccidentInsurancePercent?: number,
        public employeeMedicalInsurancePercent?: number,
        public employeeUnEmployeeInsurancePercent?: number,
        public employeeTradeUnionInsurancePercent?: number
    ) {
        this.isWorkingOnMSaturday = this.isWorkingOnMSaturday || false;
        this.isWorkingOnMSunday = this.isWorkingOnMSunday || false;
        this.isWorkingOnNSaturday = this.isWorkingOnNSaturday || false;
        this.isWorkingOnNSunday = this.isWorkingOnNSunday || false;
    }
}
