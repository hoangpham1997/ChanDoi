import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IRepository } from 'app/shared/model/repository.model';
import { IUnit } from 'app/shared/model/unit.model';
import { HandlingResult } from 'app/shared/modal/handling-result/handling-result.model';

type EntityResponseType = HttpResponse<IRepository>;
type EntityArrayResponseType = HttpResponse<IRepository[]>;

@Injectable({ providedIn: 'root' })
export class RepositoryService {
    private resourceUrl = SERVER_API_URL + 'api/repositories';

    constructor(private http: HttpClient) {}

    create(repository: IRepository): Observable<EntityResponseType> {
        return this.http.post<IRepository>(this.resourceUrl, repository, { observe: 'response' });
    }

    update(repository: IRepository): Observable<EntityResponseType> {
        return this.http.put<IRepository>(this.resourceUrl, repository, { observe: 'response' });
    }

    find(id?: string): Observable<EntityResponseType> {
        return this.http.get<IRepository>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IRepository[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    findAllByCompanyID(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IRepository[]>(this.resourceUrl + '/findAllByCompanyID', { params: options, observe: 'response' });
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    getRepositoryCombobox(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IRepository[]>(this.resourceUrl + '/combobox', { params: options, observe: 'response' });
    }

    getRepositoryComboboxGetAll(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IRepository[]>(this.resourceUrl + '/get-all', { params: options, observe: 'response' });
    }

    getRepositoryReport(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IRepository[]>(this.resourceUrl + '/forReport', { params: options, observe: 'response' });
    }

    pageableRepositories(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IRepository[]>(this.resourceUrl + '/pageable-all-repositories', { params: options, observe: 'response' });
    }

    listRepositories(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IRepository[]>(this.resourceUrl + '/list-all-repositories-combobox', { params: options, observe: 'response' });
    }

    deleteByListIDRepository(rq: any[]): Observable<HttpResponse<HandlingResult>> {
        return this.http.post<any>(`${this.resourceUrl}/delete-list-repository`, rq, { observe: 'response' });
    }
}
