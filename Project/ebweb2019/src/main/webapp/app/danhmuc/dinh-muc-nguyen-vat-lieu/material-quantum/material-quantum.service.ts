import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IMaterialQuantum } from 'app/shared/model/material-quantum.model';
import { IStatisticsCode } from 'app/shared/model/statistics-code.model';
import { IUnit } from 'app/shared/model/unit.model';
import { IObjectsMaterialQuantum } from 'app/shared/model/objects-material-quantum.model';

type EntityResponseType = HttpResponse<IMaterialQuantum>;
type EntityArrayResponseType = HttpResponse<IMaterialQuantum[]>;
type ObjectArrayResponseType = HttpResponse<IObjectsMaterialQuantum[]>;

@Injectable({ providedIn: 'root' })
export class MaterialQuantumService {
    private resourceUrl = SERVER_API_URL + 'api/material-quantums';

    constructor(private http: HttpClient) {}

    create(materialQuantum: IMaterialQuantum): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(materialQuantum);
        return this.http
            .post<IMaterialQuantum>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(materialQuantum: IMaterialQuantum): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(materialQuantum);
        return this.http
            .put<IMaterialQuantum>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IMaterialQuantum>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IMaterialQuantum[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(materialQuantum: IMaterialQuantum): IMaterialQuantum {
        const copy: IMaterialQuantum = Object.assign({}, materialQuantum, {
            fromDate:
                materialQuantum.fromDate != null && materialQuantum.fromDate.isValid()
                    ? materialQuantum.fromDate.format(DATE_FORMAT)
                    : null,
            toDate: materialQuantum.toDate != null && materialQuantum.toDate.isValid() ? materialQuantum.toDate.format(DATE_FORMAT) : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.fromDate = res.body.fromDate != null ? moment(res.body.fromDate) : null;
        res.body.toDate = res.body.toDate != null ? moment(res.body.toDate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((materialQuantum: IMaterialQuantum) => {
            materialQuantum.fromDate = materialQuantum.fromDate != null ? moment(materialQuantum.fromDate) : null;
            materialQuantum.toDate = materialQuantum.toDate != null ? moment(materialQuantum.toDate) : null;
        });
        return res;
    }

    getMaterialQuantums(): Observable<EntityArrayResponseType> {
        return this.http.get<IMaterialQuantum[]>(this.resourceUrl + '/find-all-material-quantums-by-companyid', { observe: 'response' });
    }

    getObject(): Observable<ObjectArrayResponseType> {
        return this.http.get<IObjectsMaterialQuantum[]>(this.resourceUrl + '/find-all-object-active-by-companyid', {
            observe: 'response'
        });
    }
}
