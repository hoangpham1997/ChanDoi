import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IAccountList } from 'app/shared/model/account-list.model';
import { IUnit } from 'app/shared/model/unit.model';
import { IAccountDefault } from 'app/shared/model/account-default.model';
import { ICurrency } from 'app/shared/model/currency.model';
import { IBank } from 'app/shared/model/bank.model';
import { IAccountAllList } from 'app/shared/model/account-all-list.model';
import { IOrganizationUnitOptionReport } from 'app/shared/model/organization-unit-option-report.model';
import { IExpenseItem } from 'app/shared/model/expense-item.model';
import { ICostSet } from 'app/shared/model/cost-set.model';
import { IStatisticsCode } from 'app/shared/model/statistics-code.model';

type EntityResponseType = HttpResponse<IAccountList>;
type EntityArrayResponseType = HttpResponse<IAccountList[]>;
type EntityArrayResponseAccountType = HttpResponse<IAccountAllList>;

@Injectable({ providedIn: 'root' })
export class AccountListService {
    private resourceUrl = SERVER_API_URL + 'api/account-lists';

    constructor(private http: HttpClient) {}

    create(accountList: IAccountList): Observable<EntityResponseType> {
        return this.http.post<IAccountList>(this.resourceUrl, accountList, { observe: 'response' });
    }

    update(accountList: IAccountList): Observable<EntityResponseType> {
        return this.http.put<IAccountList>(this.resourceUrl, accountList, { observe: 'response' });
    }

    find(id: any): Observable<EntityResponseType> {
        return this.http.get<IAccountList>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IAccountList[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    searchAll(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IAccountList[]>(`${this.resourceUrl}/search-all`, { params: options, observe: 'response' });
    }

    getAccountType(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IAccountList[]>(this.resourceUrl + '/getAllAccountType', {
            params: options,
            observe: 'response'
        });
    }
    getAccountTypeSecond(req?: any): Observable<EntityArrayResponseAccountType> {
        const options = createRequestOption(req);
        return this.http.get<IAccountAllList>(this.resourceUrl + '/get-all-account-type', {
            params: options,
            observe: 'response'
        });
    }

    getAccountTypeFromGOV(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IAccountList[]>(this.resourceUrl + '/get-all-account-type-from-gov', {
            params: options,
            observe: 'response'
        });
    }
    getAccountTypeThird(req?: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        return this.http.get<any>(this.resourceUrl + '/get-all-account-third', {
            params: options,
            observe: 'response'
        });
    }
    getAccountTypeFour(req?: any): Observable<HttpResponse<any>> {
        // const options = createRequestOption(req);
        return this.http.post<any>(this.resourceUrl + '/get-all-account-four', req, {
            observe: 'response'
        });
    }

    findByGOtherVoucher(): Observable<EntityArrayResponseType> {
        return this.http.get<IAccountList[]>(this.resourceUrl + '/findByGOtherVoucher', { observe: 'response' });
    }

    getAllAccounts(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IAccountList[]>(this.resourceUrl + '/getAllAccounts', {
            params: options,
            observe: 'response'
        });
    }

    findAllAccountListForCategory(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IAccountList[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    getAccountLike133(): Observable<EntityArrayResponseType> {
        return this.http.get<IAccountList[]>(this.resourceUrl + '/find-account-like-133', {
            observe: 'response'
        });
    }

    /**
     * @author phuonghv find Account List by Account type
     * @param ppServiceType 240: PP_SERVICE_UNPAID, 241: PP_SERVICE_CASH,
     *                     242: PP_SERVICE_PAYMENT_ORDER, 243: PPSERVICE_CHECK_TRANSFER
     *                     244: PP_SERVICE_CREDIT_CARD, 245: PPSERVICE_CASH_CHECK
     * @param accountType 0 - debit , 1 - credit
     * @return
     */

    getAccountListByAccountType(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IAccountList[]>(`${this.resourceUrl}/get-accountlist-by-account-type`, {
            params: options,
            observe: 'response'
        });
    }
    findAllByCompanyID(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IAccountList[]>(`${this.resourceUrl}/find-all-by-company-id`, { params: options, observe: 'response' });
    }

    getAccountListsActive(): Observable<EntityArrayResponseType> {
        return this.http.get<IAccountList[]>(this.resourceUrl + '/find-all-account-lists-active-companyid', { observe: 'response' });
    }

    getAccountListsActiveExceptID(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IAccountList[]>(this.resourceUrl + '/find-all-account-lists-active-companyid-except-id', {
            params: options,
            observe: 'response'
        });
    }

    getAccountActive(): Observable<EntityArrayResponseType> {
        return this.http.get<IAccountList[]>(this.resourceUrl + '/find-all-account-active-company-id', { observe: 'response' });
    }

    getAccountActive1(): Observable<EntityArrayResponseType> {
        return this.http.get<IAccountList[]>(this.resourceUrl + '/find-all-account-active', { observe: 'response' });
    }

    findAllForSystemOptions(): Observable<EntityArrayResponseType> {
        return this.http.get<IAccountList[]>(this.resourceUrl + '/find-all-for-system-options', { observe: 'response' });
    }

    getAccountListsActiveAndOP(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IAccountList[]>(this.resourceUrl + '/find-all-account-lists-active-companyid-and-op', {
            params: options,
            observe: 'response'
        });
    }

    getAccountLists(): Observable<EntityArrayResponseType> {
        return this.http.get<IAccountList[]>(this.resourceUrl + '/find-all-account-lists-companyid', { observe: 'response' });
    }

    getAccountListsByOrg(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IAccountList[]>(this.resourceUrl + '/find-all-account-lists-by-org', { params: options, observe: 'response' });
    }

    getAccountStartWith154(): Observable<EntityArrayResponseType> {
        return this.http.get<IAccountList[]>(this.resourceUrl + '/find-all-account-start-with-154', { observe: 'response' });
    }

    getAccountStartWith112(): Observable<EntityArrayResponseType> {
        return this.http.get<IAccountList[]>(this.resourceUrl + '/find-all-account-start-with-112', { observe: 'response' });
    }

    getAccountStartWith111(): Observable<EntityArrayResponseType> {
        return this.http.get<IAccountList[]>(this.resourceUrl + '/find-all-account-start-with-111', { observe: 'response' });
    }

    getAccountForAccountDefault(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IAccountList[]>(this.resourceUrl + '/find-all-for-account-default', { params: options, observe: 'response' });
    }

    getAccountForOrganizationUnit(): Observable<EntityArrayResponseType> {
        return this.http.get<IAccountList[]>(this.resourceUrl + '/get-account-for-organization-unit', { observe: 'response' });
    }

    deleteAccountList(req?: any): Observable<EntityResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IAccountList>(`${this.resourceUrl}/delete-by-account-list-id`, {
            params: options,
            observe: 'response'
        });
    }

    exportResultOP(): Observable<HttpResponse<any>> {
        let headers = new HttpHeaders();
        headers = headers.set('Accept', 'application/x-excel');
        return this.http.get(this.resourceUrl + '/export/pdf', { observe: 'response', headers, responseType: 'blob' });
    }

    getAccountDetailType(): Observable<EntityArrayResponseType> {
        return this.http.get<IAccountList[]>(this.resourceUrl + '/find-all-account-by-detailType', { observe: 'response' });
    }

    getAccountDetailTypeActive(): Observable<EntityArrayResponseType> {
        return this.http.get<IAccountList[]>(this.resourceUrl + '/find-all-account-by-detail-type-active', { observe: 'response' });
    }

    getGradeAccount(): Observable<HttpResponse<any[]>> {
        return this.http.get<any[]>(this.resourceUrl + '/get-grade-account', { observe: 'response' });
    }

    checkOPNAndExistData(req?: any): Observable<EntityResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IAccountList>(`${this.resourceUrl}/check-opn-and-exist-data`, {
            params: options,
            observe: 'response'
        });
    }

    getAccount(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICostSet[]>(`${this.resourceUrl}/find-all-account-dependent`, { params: options, observe: 'response' });
    }

    getAccountListSimilarBranch(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IAccountList[]>(this.resourceUrl + '/find-all-account-list-by-company-id-similar-branch', {
            params: options,
            observe: 'response'
        });
    }

    getAccountForTHCPTheoKMCP() {
        return this.http.get<IAccountList[]>(this.resourceUrl + '/find-all-account-for-thcp-theo-kmcp', { observe: 'response' });
    }

    getAllAccountList(req?: any): Observable<EntityArrayResponseType> {
        return this.http.get<IAccountList[]>(`${this.resourceUrl}/find-all-account-list-active-and-accounting-type`, {
            observe: 'response'
        });
    }
}
