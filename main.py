import dash
from dash import Dash, dash_table, html, dcc, Input, Output
import pandas as pd
import requests

app = Dash(__name__)

app.layout = html.Div([
    html.Div([
        html.Label("Enter Warehouse ID:"),
        dcc.Input(id="warehouse-id", type="text", value="001", debounce=True),
    ], style={"marginBottom": "20px"}),

    dash_table.DataTable(
        id="table",
        sort_action="native",
        filter_action="native",
        page_size=10,
        style_table={"overflowX": "auto", "minWidth": "100%"},
        style_cell={
            "textAlign": "left",
            "whiteSpace": "normal",
            "height": "auto",
            "padding": "10px",
            "minWidth": "150px"
        },
        style_header={
            "fontWeight": "bold",
            "backgroundColor": "#f0f0f0"
        },
        style_data_conditional=[
            {"if": {"row_index": "odd"}, "backgroundColor": "#f9f9f9"}
        ]
    )
])


@app.callback(
    Output("table", "columns"),
    Output("table", "data"),
    Input("warehouse-id", "value")
)
def update_table(warehouse_id):
    if not warehouse_id:
        return [], []

    url = f"http://localhost:8080/warehouse/{warehouse_id}/json"
    try:
        data = requests.get(url).json()
        products = data["productData"]
        warehouse_info = {k: v for k, v in data.items() if k != "productData"}

        df = pd.DataFrame(products)
        for key, value in warehouse_info.items():
            df[key] = value

        columns = [{"name": i, "id": i} for i in df.columns]
        records = df.to_dict("records")
        return columns, records
    except Exception as e:
        return [{"name": "Error", "id": "Error"}], [{"Error": str(e)}]


if __name__ == "__main__":
    app.run(debug=True)
