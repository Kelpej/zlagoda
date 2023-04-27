import {Component, EventEmitter, Output} from '@angular/core';

@Component({
  selector: 'search-bar',
  templateUrl: './search-bar.component.html',
  styleUrls: ['./search-bar.component.scss']
})
export class SearchBarComponent {
  public value: string = '';
  @Output() search = new EventEmitter<string>();

  searchInputChanged() {
    this.search.emit(this.value);
  }
}
