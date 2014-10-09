 How to use:
 -
 Nothing special, use it as normal listviews, just remember to implement IInfinityAdapter to the adapter.
 
     InfinityListView listView = (InfinityListView) rootView.findViewById(R.id.listView);
     List<LiveItem> items = new ArrayList<LiveItem>();
     LiveAdapter adapter = new LiveAdapter(getAppContext(), items);
     listView.setEmptyView(rootView.findViewById(R.id.empty_list));