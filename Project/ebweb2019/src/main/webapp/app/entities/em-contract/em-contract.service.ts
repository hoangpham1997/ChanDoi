import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IEMContract } from 'app/shared/model/em-contract.model';
import { IStatisticsCode } from 'app/shared/model/statistics-code.model';

type EntityResponseType = HttpResponse<IEMContract>;
type EntityArrayResponseType = HttpResponse<IEMContract[]>;

@Injectable({ providedIn: 'root' })
export class EMContractService {
    private resourceUrl = SERVER_API_URL + 'api/e-m-contracts';

    constructor(private http: HttpClient) {}

    create(eMContract: IEMContract): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(eMContract);
        return this.http
            .post<IEMContract>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(eMContract: IEMContract): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(eMContract);
        return this.http
            .put<IEMContract>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IEMContract>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IEMContract[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    getAllEMContracts(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IEMContract[]>(this.resourceUrl + '/getAllEMContracts', { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(eMContract: IEMContract): IEMContract {
        const copy: IEMContract = Object.assign({}, eMContract, {
            signedDate: eMContract.signedDate != null && eMContract.signedDate.isValid() ? eMContract.signedDate.format(DATE_FORMAT) : null,
            startedDate:
                eMContract.startedDate != null && eMContract.startedDate.isValid() ? eMContract.startedDate.format(DATE_FORMAT) : null,
            closedDate: eMContract.closedDate != null && eMContract.closedDate.isValid() ? eMContract.closedDate.format(DATE_FORMAT) : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.signedDate = res.body.signedDate != null ? moment(res.body.signedDate) : null;
        res.body.startedDate = res.body.startedDate != null ? moment(res.body.startedDate) : null;
        res.body.closedDate = res.body.closedDate != null ? moment(res.body.closedDate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((eMContract: IEMContract) => {
            eMContract.signedDate = eMContract.signedDate != null ? moment(eMContract.signedDate) : null;
            eMContract.startedDate = eMContract.startedDate != null ? moment(eMContract.startedDate) : null;
            eMContract.closedDate = eMContract.closedDate != null ? moment(eMContract.closedDate) : null;
        });
        return res;
    }

    getAllEMContractsActive(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IEMContract[]>(this.resourceUrl + '/active', { params: options, observe: 'response' });
    }

    getEMContracts(): Observable<EntityArrayResponseType> {
        return this.http.get<IEMContract[]>(this.resourceUrl + '/find-all-em-contracts-active-companyid', { observe: 'response' });
    }

    getEMContractsForReport(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IEMContract[]>(this.resourceUrl + '/find-all-em-contracts-for-report', {
            params: options,
            observe: 'response'
        });
    }
}
