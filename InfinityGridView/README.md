How to use:
-

When users scroll to the bottom, there will be displayed a button, click to the button will load more item to the grid.
Make sure you implement IInfinityAdapter for the adapter to use in this GridView.

Example of usage:

	View rootView = inflater.inflate(R.layout.fragment_main, container, false);
    InfinityGridView gridview = (InfinityGridView) rootView.findViewById(R.id.gridview);

    gridview.setLoadMoreBtn(rootView.findViewById(R.id.btnLoadMore));
    gridview.setLoadingView(rootView.findViewById(R.id.loadingView));

    List<MovieItem> data = new ArrayList<MovieItem>();

    final ImageAdapter adapter = new ImageAdapter(activity, data);
    gridview.setAdapter(adapter);