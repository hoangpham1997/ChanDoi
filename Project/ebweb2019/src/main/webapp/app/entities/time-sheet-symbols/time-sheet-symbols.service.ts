import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ITimeSheetSymbols } from 'app/shared/model/time-sheet-symbols.model';

type EntityResponseType = HttpResponse<ITimeSheetSymbols>;
type EntityArrayResponseType = HttpResponse<ITimeSheetSymbols[]>;

@Injectable({ providedIn: 'root' })
export class TimeSheetSymbolsService {
    private resourceUrl = SERVER_API_URL + 'api/time-sheet-symbols';

    constructor(private http: HttpClient) {}

    create(timeSheetSymbols: ITimeSheetSymbols): Observable<EntityResponseType> {
        return this.http.post<ITimeSheetSymbols>(this.resourceUrl, timeSheetSymbols, { observe: 'response' });
    }

    update(timeSheetSymbols: ITimeSheetSymbols): Observable<EntityResponseType> {
        return this.http.put<ITimeSheetSymbols>(this.resourceUrl, timeSheetSymbols, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ITimeSheetSymbols>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ITimeSheetSymbols[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
