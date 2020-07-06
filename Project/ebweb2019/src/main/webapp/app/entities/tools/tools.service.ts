import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ITools } from 'app/shared/model/tools.model';

type EntityResponseType = HttpResponse<ITools>;
type EntityArrayResponseType = HttpResponse<ITools[]>;

@Injectable({ providedIn: 'root' })
export class ToolsService {
    private resourceUrl = SERVER_API_URL + 'api/tools';

    constructor(private http: HttpClient) {}

    create(tools: ITools): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(tools);
        return this.http
            .post<ITools>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(tools: ITools): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(tools);
        return this.http
            .put<ITools>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<ITools>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ITools[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    queryAllTools(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ITools[]>(this.resourceUrl + '/get-all', { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    getAllTools(): Observable<EntityArrayResponseType> {
        return this.http
            .get<ITools[]>(this.resourceUrl + '/get-all-tool', { observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(tools: ITools): ITools {
        const copy: ITools = Object.assign({}, tools, {
            postedDate: tools.postedDate != null && tools.postedDate.isValid() ? tools.postedDate.format(DATE_FORMAT) : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.postedDate = res.body.postedDate != null ? moment(res.body.postedDate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((tools: ITools) => {
            tools.postedDate = tools.postedDate != null ? moment(tools.postedDate) : null;
        });
        return res;
    }
}
