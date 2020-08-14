##	TODO
* Change how recycler view orders items
* Add field to db for order in recycler view (Linked list ? ~ Each note points to the next in order +DB FIeld +Note Obj Field)
	* On create have field for the final note (Easy note addistion)
	* Find Head Select * From TABLE WHERE ID NOT IN List OF ID_NEXT_NOTE
	* Undo Feature 
		* If next element is first point to it if next element -1 add and change prev -1 to added
			else reinsert and change prev element next to current 