import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IPSSalaryTaxInsuranceRegulation } from 'app/shared/model/ps-salary-tax-insurance-regulation.model';

type EntityResponseType = HttpResponse<IPSSalaryTaxInsuranceRegulation>;
type EntityArrayResponseType = HttpResponse<IPSSalaryTaxInsuranceRegulation[]>;

@Injectable({ providedIn: 'root' })
export class PSSalaryTaxInsuranceRegulationService {
    private resourceUrl = SERVER_API_URL + 'api/ps-salary-tax-insurance-regulations';

    constructor(private http: HttpClient) {}

    create(pSSalaryTaxInsuranceRegulation: IPSSalaryTaxInsuranceRegulation): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(pSSalaryTaxInsuranceRegulation);
        return this.http
            .post<IPSSalaryTaxInsuranceRegulation>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(pSSalaryTaxInsuranceRegulation: IPSSalaryTaxInsuranceRegulation): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(pSSalaryTaxInsuranceRegulation);
        return this.http
            .put<IPSSalaryTaxInsuranceRegulation>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IPSSalaryTaxInsuranceRegulation>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IPSSalaryTaxInsuranceRegulation[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(pSSalaryTaxInsuranceRegulation: IPSSalaryTaxInsuranceRegulation): IPSSalaryTaxInsuranceRegulation {
        const copy: IPSSalaryTaxInsuranceRegulation = Object.assign({}, pSSalaryTaxInsuranceRegulation, {
            fromDate:
                pSSalaryTaxInsuranceRegulation.fromDate != null && pSSalaryTaxInsuranceRegulation.fromDate.isValid()
                    ? pSSalaryTaxInsuranceRegulation.fromDate.format(DATE_FORMAT)
                    : null,
            toDate:
                pSSalaryTaxInsuranceRegulation.toDate != null && pSSalaryTaxInsuranceRegulation.toDate.isValid()
                    ? pSSalaryTaxInsuranceRegulation.toDate.format(DATE_FORMAT)
                    : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.fromDate = res.body.fromDate != null ? moment(res.body.fromDate) : null;
        res.body.toDate = res.body.toDate != null ? moment(res.body.toDate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((pSSalaryTaxInsuranceRegulation: IPSSalaryTaxInsuranceRegulation) => {
            pSSalaryTaxInsuranceRegulation.fromDate =
                pSSalaryTaxInsuranceRegulation.fromDate != null ? moment(pSSalaryTaxInsuranceRegulation.fromDate) : null;
            pSSalaryTaxInsuranceRegulation.toDate =
                pSSalaryTaxInsuranceRegulation.toDate != null ? moment(pSSalaryTaxInsuranceRegulation.toDate) : null;
        });
        return res;
    }
}
