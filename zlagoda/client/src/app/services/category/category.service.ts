import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Product} from "../product/product.service";

export type Category = {
  id: number,
  name: string,
}

@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  constructor(private http: HttpClient) { }

  private readonly apiUrl = '/categories'

  public getAll(): Observable<Category[]> {
    return this.http.get<Category[]>(this.apiUrl);
  }

  public create(category: Category): Observable<Category> {
    return this.http.post<Category>(this.apiUrl,  JSON.stringify(category));
  }

  public edit(category: Category): Observable<Category> {
    return this.http.put<Category>(this.apiUrl, JSON.stringify(category));
  }

  public delete(category: Category): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${category.id}`);
  }
}
