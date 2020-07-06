import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IFixedAsset } from 'app/shared/model/fixed-asset.model';

type EntityResponseType = HttpResponse<IFixedAsset>;
type EntityArrayResponseType = HttpResponse<IFixedAsset[]>;

@Injectable({ providedIn: 'root' })
export class FixedAssetService {
    private resourceUrl = SERVER_API_URL + 'api/fixed-assets';

    constructor(private http: HttpClient) {}

    create(fixedAsset: IFixedAsset): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(fixedAsset);
        return this.http
            .post<IFixedAsset>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(fixedAsset: IFixedAsset): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(fixedAsset);
        return this.http
            .put<IFixedAsset>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IFixedAsset>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IFixedAsset[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(fixedAsset: IFixedAsset): IFixedAsset {
        const copy: IFixedAsset = Object.assign({}, fixedAsset, {
            deliveryRecordDate:
                fixedAsset.deliveryRecordDate != null && fixedAsset.deliveryRecordDate.isValid()
                    ? fixedAsset.deliveryRecordDate.format(DATE_FORMAT)
                    : null,
            purchasedDate:
                fixedAsset.purchasedDate != null && fixedAsset.purchasedDate.isValid()
                    ? fixedAsset.purchasedDate.format(DATE_FORMAT)
                    : null,
            incrementDate:
                fixedAsset.incrementDate != null && fixedAsset.incrementDate.isValid()
                    ? fixedAsset.incrementDate.format(DATE_FORMAT)
                    : null,
            depreciationDate:
                fixedAsset.depreciationDate != null && fixedAsset.depreciationDate.isValid()
                    ? fixedAsset.depreciationDate.format(DATE_FORMAT)
                    : null,
            usedDate: fixedAsset.usedDate != null && fixedAsset.usedDate.isValid() ? fixedAsset.usedDate.format(DATE_FORMAT) : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.deliveryRecordDate = res.body.deliveryRecordDate != null ? moment(res.body.deliveryRecordDate) : null;
        res.body.purchasedDate = res.body.purchasedDate != null ? moment(res.body.purchasedDate) : null;
        res.body.incrementDate = res.body.incrementDate != null ? moment(res.body.incrementDate) : null;
        res.body.depreciationDate = res.body.depreciationDate != null ? moment(res.body.depreciationDate) : null;
        res.body.usedDate = res.body.usedDate != null ? moment(res.body.usedDate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((fixedAsset: IFixedAsset) => {
            fixedAsset.deliveryRecordDate = fixedAsset.deliveryRecordDate != null ? moment(fixedAsset.deliveryRecordDate) : null;
            fixedAsset.purchasedDate = fixedAsset.purchasedDate != null ? moment(fixedAsset.purchasedDate) : null;
            fixedAsset.incrementDate = fixedAsset.incrementDate != null ? moment(fixedAsset.incrementDate) : null;
            fixedAsset.depreciationDate = fixedAsset.depreciationDate != null ? moment(fixedAsset.depreciationDate) : null;
            fixedAsset.usedDate = fixedAsset.usedDate != null ? moment(fixedAsset.usedDate) : null;
        });
        return res;
    }
}
