import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';
import { IBank } from 'app/shared/model/bank.model';
import { IReceiveBill } from 'app/shared/model/receive-bill.model';
import { Moment } from 'moment';
import { IOrganizationUnitOptionReport } from 'app/shared/model/organization-unit-option-report.model';
import { ITreeOrganizationUnit } from 'app/shared/model/organization-unit-tree.model';
import { IAccountList } from 'app/shared/model/account-list.model';

type EntityResponseType = HttpResponse<IOrganizationUnit>;
type EntityArrayResponseType = HttpResponse<IOrganizationUnit[]>;
type EntityArrayTreeResponseType = HttpResponse<ITreeOrganizationUnit[]>;

@Injectable({ providedIn: 'root' })
export class OrganizationUnitAdminService {
    private resourceUrl = SERVER_API_URL + 'api/organization-units';
    private publicResourceUrl = SERVER_API_URL + 'api/p/organization-units';

    constructor(private http: HttpClient) {}

    create(organizationUnit: IOrganizationUnit): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(organizationUnit);
        return this.http
            .post<IOrganizationUnit>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    createBigOrg(organizationUnit: IOrganizationUnit): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(organizationUnit);
        return this.http
            .post<IOrganizationUnit>(this.resourceUrl + '/big-org', copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    getOrganizationUnitsBySearch(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IOrganizationUnit[]>(this.resourceUrl + '/find-all-organization-units-search', { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    update(organizationUnit: IOrganizationUnit): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(organizationUnit);
        return this.http
            .put<IOrganizationUnit>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    updateOrg(organizationUnit: IOrganizationUnit): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(organizationUnit);
        return this.http
            .put<IOrganizationUnit>(this.resourceUrl + '/big-org', copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: string): Observable<EntityResponseType> {
        return this.http
            .get<IOrganizationUnit>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IOrganizationUnit[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    queryBigOrg(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IOrganizationUnit[]>(this.resourceUrl + '/big-org', { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    queryListBigOrg(req?: any): Observable<any> {
        const options = createRequestOption(req);
        return this.http
            .get<any>(this.resourceUrl + '/list-big-org', { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    queryListUserPackage(req?: any): Observable<any> {
        const options = createRequestOption(req);
        return this.http
            .get<any>(this.resourceUrl + '/list-user-package', { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    getAllOrganizationUnits(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IOrganizationUnit[]>(this.resourceUrl + '/getAllOrganizationUnits', {
                params: options,
                observe: 'response'
            })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    getAllOrganizationUnitsActive(): Observable<EntityArrayResponseType> {
        return this.http.get<IOrganizationUnit[]>(this.resourceUrl + '/getAllOrganizationUnits/active', { observe: 'response' });
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(organizationUnit: IOrganizationUnit): IOrganizationUnit {
        const copy: IOrganizationUnit = Object.assign({}, organizationUnit, {
            issueDate:
                organizationUnit.issueDate != null && organizationUnit.issueDate.isValid()
                    ? organizationUnit.issueDate.format(DATE_FORMAT)
                    : null,
            fromDate:
                organizationUnit.fromDate != null && organizationUnit.fromDate.isValid()
                    ? organizationUnit.fromDate.format(DATE_FORMAT)
                    : null,
            toDate:
                organizationUnit.toDate != null && organizationUnit.toDate.isValid() ? organizationUnit.toDate.format(DATE_FORMAT) : null,
            startDate:
                organizationUnit.startDate != null && organizationUnit.startDate.isValid()
                    ? organizationUnit.startDate.format(DATE_FORMAT)
                    : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.issueDate = res.body.issueDate != null ? moment(res.body.issueDate) : null;
        res.body.fromDate = res.body.fromDate != null ? moment(res.body.fromDate) : null;
        res.body.toDate = res.body.toDate != null ? moment(res.body.toDate) : null;
        res.body.startDate = res.body.startDate != null ? moment(res.body.startDate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((organizationUnit: IOrganizationUnit) => {
            organizationUnit.issueDate = organizationUnit.issueDate != null ? moment(organizationUnit.issueDate) : null;
            organizationUnit.fromDate = organizationUnit.fromDate != null ? moment(organizationUnit.fromDate) : null;
            organizationUnit.toDate = organizationUnit.toDate != null ? moment(organizationUnit.toDate) : null;
            organizationUnit.startDate = organizationUnit.startDate != null ? moment(organizationUnit.startDate) : null;
        });
        return res;
    }

    findByUnitType(req: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IOrganizationUnit[]>(`${SERVER_API_URL}/api/p/organization-units/unit-type`, {
                params: options,
                observe: 'response'
            })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    getAllBudgetItemActive(): Observable<EntityResponseType> {
        return this.http.get<any>(`${this.resourceUrl}/active`, { observe: 'response' });
    }

    getOrganizationUnits(): Observable<EntityArrayResponseType> {
        return this.http.get<IOrganizationUnit[]>(this.resourceUrl + '/find-all-organization-units-active-companyid', {
            observe: 'response'
        });
    }

    recursiveOrganizationUnitByParentID(): Observable<EntityArrayResponseType> {
        return this.http.get<IOrganizationUnit[]>(this.resourceUrl + '/recursive-organization-units-by-parent-id', {
            observe: 'response'
        });
    }

    getAllOrganizationUnitByParentID(): Observable<EntityArrayResponseType> {
        return this.http.get<IOrganizationUnit[]>(this.resourceUrl + '/get-list-organization-units', {
            observe: 'response'
        });
    }

    getTreeOrganizationUnits(): Observable<EntityArrayTreeResponseType> {
        return this.http.get<ITreeOrganizationUnit[]>(this.resourceUrl + '/get-tree-organization-unit', {
            observe: 'response'
        });
    }

    getAllTreeOrganizationUnits(): Observable<EntityArrayTreeResponseType> {
        return this.http.get<ITreeOrganizationUnit[]>(this.resourceUrl + '/get-all-tree-organization-unit', {
            observe: 'response'
        });
    }

    getPostedDate(): Observable<HttpResponse<Moment>> {
        return this.http.get<Moment>(this.resourceUrl + '/get-posted-date', {
            observe: 'response'
        });
    }

    getOrganizationUnitsByCompanyID(): Observable<EntityArrayResponseType> {
        return this.http.get<IOrganizationUnit[]>(this.resourceUrl + '/find-all-organization-units-by-companyid', {
            observe: 'response'
        });
    }

    getOuTree(): Observable<any> {
        return this.http.get<any>(this.publicResourceUrl + '/tree', { observe: 'response' });
    }

    deleteOrganizationUnitByCompanyID(req?: any): Observable<EntityResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IOrganizationUnitOptionReport>(`${this.resourceUrl}/delete-by-organization-unit-id`, {
            params: options,
            observe: 'response'
        });
    }

    deleteBigOrganizationUnitByCompanyID(req?: any): Observable<EntityResponseType> {
        const options = createRequestOption(req);
        return this.http.delete<IOrganizationUnitOptionReport>(`${this.resourceUrl}/delete-by-big-organization-unit-id`, {
            params: options,
            observe: 'response'
        });
    }

    getAllOuTree(): Observable<any> {
        return this.http.get<any>(this.publicResourceUrl + '/tree/getAll', { observe: 'response' });
    }

    getAllOuTreeByOrgId(): Observable<any> {
        return this.http.get<any>(this.publicResourceUrl + '/tree/getAll-By-OrgID', { observe: 'response' });
    }

    // add by anmt
    findAllByListID(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IOrganizationUnit[]>(this.resourceUrl + '/find-all-by-list-id', { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    getOrganizationUnitsActiveExceptID(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IOrganizationUnit[]>(this.resourceUrl + '/find-all-organization-units-active-companyid-except-id', {
            params: options,
            observe: 'response'
        });
    }

    queryTotalOrg(req?: any): Observable<any> {
        const options = createRequestOption(req);
        return this.http
            .get<any>(this.resourceUrl + '/list-total-big-org', { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }
}
